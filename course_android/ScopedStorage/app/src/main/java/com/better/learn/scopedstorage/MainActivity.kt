package com.better.learn.scopedstorage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets


/**
 * --------------------------------------------------------------------------------
 * android Q前的存储划分：三块：外部存储、内部存储私有目录、外部存储的APP目录
 * 外部存储：卸载APP不会自动删除
 * (有文件权限就可以访问) /sdcard/xxx,
 * APP存储目录：卸载APP会自动删除
 * 1. 内部存储私有目录， /data/data/包名/，
 * 2. 外部存储的APP目录：/storage/Android/data/包名/files
 * 访问机制：
 * 1. 内部存储私有目录只能自己访问
 * 2. 外部sdcard目录
 * 2.1 外部存储的APP目录，不需要存储权限，自己就可以访问，
 * 2.2 外部的其他目录包括其他APP的"外部存储的APP目录”，只要有存储权限都可以访问
 * ------
 * android Q上的目录：分为两块：私有目录、共有目录
 * 私有目录：卸载APP会自动删除
 * 1. /storage/emulated/0/Android/data/包名/files
 * 2. /data/data/包名/
 * 公共目录：卸载APP不会自动删除
 * Downloads、Documents、Pictures 、DCIM、Movies、Music、Ringtones
 * 路径：/storage/emulated/0/Downloads
 * 访问机制：
 * 采用沙盒机制
 * 1. APP可以访问自己的目录下的文件，不需要存储权限（本来之前版本也是这样）
 * 2. 公共目录，
 * 2.1 只能访问MediaStore提供的流文件（媒体文件，发现也可以通过MediaStore存储非流文件
 * 到Download目录哒），或者SAF访问用户同一的公共文件
 * 2.2 功能目录可以没有权限的时候访问自己创建的文件。（这个是变动）
 * --------------------------------------------------------------------------------
 * ## 适配方案
 * 如果下载APP要保存下来的文件：
 * 1. 流文件可以存放在MediaStore对应的文件夹中（Pictures、Video、Audio、）：图片、视频、音频
 * 2. 非流文件放在MediaStore对应的Download目录下
 * 如果卸载APP后不需要保留的文件：
 * 直接放在私有目录(getExternalFilesDirs或者内存存储的私有目录)下就行
 * ## 外部目录操作文件方式：
 * 1. 使用 ContentProvider 增删改查
 * 2. 使用SAF选中文件或文件夹，返回的也是uri，差不多还是用CP来操作结果
 * 3. 只有MediaStore中的文件，访问其他APP创建的文件需要权限，自己的则不需要。所以只有它才是无侵入，
 * 的方式操作文件，既不需要用户选择，确认。而SAF方式需要用户选择，这就麻烦了，不能程序自己执行。
 *
 * Q:怎样在外部存储中创建目录
 * A:想在外部存储直接创建一个文件夹是莫法的，只能在MediaStore对应的目录使用 RELATIVE_PATH，
 * 创建目录可行，譬如下面的列子创建了Pictures/test Download/test 目录
 * （测试）T: 重命名applicationID
 * （结果）R：不同应用在相同的RELATIVE_PATH，是可以存在的，文件会自动创建新的
 *-----
 *  android:requestLegacyExternalStorage="false" 默认是 false，表示已经支持完了，直接用分区模式
 * 常见mime-type: https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
 * 参考文档：
 * https://developer.android.com/about/versions/10/privacy/changes#scoped-storage
 * https://developer.android.com/training/data-storage/files/external-scoped
 * https://developer.android.com/guide/topics/providers/document-provider
 * https://developer.android.com/training/data-storage/shared/media
 * https://developer.android.com/training/data-storage/shared/documents-files
 * https://developer.android.com/training/data-storage/use-cases#show-all-folder
 * 实践：
 * https://mp.weixin.qq.com/s/djTZykAvPc3uWcdvAjHZMw
 * https://open.oppomobile.com/wiki/doc#id=10432
 * https://medium.com/androiddevelopers/android-11-storage-faq-78cefea52b7c
 * documentfile:
 * https://developer.android.com/jetpack/androidx/releases/documentfile
 * https://github.com/android/storage-samples
 *
 * 在manifest设置需要注意
 * targetSDK = 29, 默认开启 Scoped Storage, 但可通过在 manifest 里添加 requestLegacyExternalStorage = true 关闭
 * targetSDK < 29, 默认不开启 Scoped Storage, 但可通过在 manifest 里添加requestLegacyExternalStorage = false 打开
 *
 */
class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "Better"
        const val REQ_CREATE_DIR = 100
        const val REQ_CHOOSE_IMG = 101
        const val REQ_SAF_CHOOSE_DIR = 102
        private const val image_name = "Image2.png"
        private const val DIR_NAME = "BBBB"
        fun oldWay() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
                /**
                 * isExternalStorageLegacy():
                 * true : 应用以兼容模式运行
                 * false：应用以分区存储特性运行
                 */
                Environment.isExternalStorageLegacy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 下面操作，基本上来说没有提示，则算成功

        btn_create_dir_29.setOnClickListener {
            androidQAccessSdcard()
        }
        btn_saf_dir.setOnClickListener {
            safCreateDirAndFile()
        }
        btn_create_dir_self.setOnClickListener {
            accessAppSpec()
        }
        btn_sdcard_dir.setOnClickListener {
            justAccessSd()
        }
        btn_saf_choose_img.setOnClickListener {
            chooseImage()
        }
        btn_media_store_file.setOnClickListener {
            assessPublicDocument()
        }
        btn_media_store_img.setOnClickListener {
            assessPublicImage()
        }
        btn_media_store_load_img.setOnClickListener {
            loadJustCreateImage()
        }
        btn_media_store_load_all_img.setOnClickListener {
            startActivity(Intent(this, LoadPicturesActivity::class.java))
        }
        catch { listPicturesFiles() }
    }

    /**
     * 尝试直接列出Pictures/test文件夹中的文件
     * 这是一种取巧的方式，不需要打扰用户就可以访问自己外部目录的文件
     */
    @SuppressLint("Recycle")
    private fun listPicturesFiles() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }
        val only = true
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = if (only) {
            MediaStore.Images.Media.RELATIVE_PATH + "=?"
        } else {
            null
        }
        // 注意这里要区分 / 结尾
        val args = if (only) {
            arrayOf("Pictures/test/")
        } else {
            null
        }
        // 红米 note 7 pro 上 得出来的projection 有38 个之多，所以一般还是固定要查找的列数据
        Log.e(TAG, "query uri $external")
        contentResolver.query(
            external,
            null, selection, args, null
        )?.use { cursor ->
            Log.e(TAG, "${cursor.count}")
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            // 绝对路径
            val columnIndexData = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            val columnIndexDisplayName =
                cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val columnIndexMimeType = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
            val columnIndexRPath = cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH)
            while (cursor.moveToNext()) {
                val uri =
                    ContentUris.withAppendedId(external, cursor.getLong(columnIndex))
                val data = cursor.getString(columnIndexData)
                val name =
                    cursor.getString(columnIndexDisplayName)
                val mimeType =
                    cursor.getString(columnIndexMimeType)
                val rPath =
                    cursor.getString(columnIndexRPath)
                Log.e(TAG, "$mimeType,$name,$rPath,$data,$uri")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQ_CREATE_DIR -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    createDir(data)
                }
            }
            REQ_CHOOSE_IMG -> {
                if (Activity.RESULT_OK == resultCode) {
                    data?.data?.let { uri ->
                        loadImageFromUri(uri, img_choosed_img)
                    }
                }
            }
            REQ_SAF_CHOOSE_DIR -> {
                if (requestCode == REQ_SAF_CHOOSE_DIR && Activity.RESULT_OK == resultCode) {
                    safCreateDirAndFile(data)
                }
            }
        }
    }

    /**
     * 尝试使用用户选中的dir操作文件，只能使用DocumentFile
     */
    private fun safCreateDirAndFile(data: Intent?): Boolean {
        val treeUri = data?.data ?: return true
        val takeFlags =
            (data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentResolver.takePersistableUriPermission(treeUri, takeFlags)
        } else {
            TODO("VERSION.SDK_INT < KITKAT")
        }
        //content://com.android.externalstorage.documents/tree/primary%3ADownload
        Log.e("Better", treeUri.toString())
        Log.e("Better", "${FileUtil.getFullPathFromTreeUri(treeUri, this)}")
        // 5.0 DocumentFile.fromTreeUri
        // 4.4 DocumentFile.fromSingleUri
        DocumentFile.fromTreeUri(this, treeUri)?.let { documentFile ->
            val directName = DIR_NAME
            val exist = documentFile.findFile(directName)?.run {
                exists() && isDirectory
            } ?: false
            val directoryDocumentFile = if (exist) {
                documentFile.findFile(directName)
            } else {
                documentFile.createDirectory(directName)
            }
            Log.e("Better", "creste dir: ${directoryDocumentFile?.toString()}")
            // 创建文件
            directoryDocumentFile?.let {
                val createFile = it.createFile("application/text", "helloSaf.txt")
                Log.e("Better", "create file ${createFile?.exists()}")
                createFile?.let { helloFile ->
                    contentResolver.openOutputStream(helloFile.uri)?.apply {
                        catch { write("hello thank you".toByteArray()) }
                        close()
                    }
                }
                Toast.makeText(
                    this,
                    "create file  ${createFile?.exists()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // 以分区存储的方式，尝试用File 类操作
        val path = FileUtil.getFullPathFromTreeUri(treeUri, this)
        if (null == path || path == File.pathSeparator) {
            // 不支持的路径
            Log.e("Better", "not support directory path")
        }
        // 居然可以操作！！！
        val directoryFile = File(path, DIR_NAME + File.separator + "aaa.txt")
        catch {
            directoryFile.createNewFile()
            directoryFile.outputStream().apply {
                catch { write("hello thank you".toByteArray()) }
                close()
            }
        }
        return false
    }

    /**
     * 如果使用兼容模式android:requestLegacyExternalStorage="true"
     * 需要在manifest中修改下
     * 可以像之前一样，授权存储权限后就直接操作sdcard
     */
    private fun androidQAccessSdcard() {
        val sdkInt = Build.VERSION.SDK_INT
        val have = if (sdkInt < Build.VERSION_CODES.M) {
            true
        } else {
            (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED).also {
                if (!it) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        99
                    )
                }
            }
        }
        if (have) {
            val bbbb = File(Environment.getExternalStorageDirectory(), DIR_NAME)
            if (!bbbb.exists()) {
                bbbb.mkdir()
            }
            val testFile = File(bbbb, "test.txt")
            val suc = if (testFile.exists()) {
                true
            } else {
                testFile.createNewFile()
            }
            Toast.makeText(this, "create file: $suc", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 在scopeStorage 模式下 android:requestLegacyExternalStorage="false"
     * 在选中的文件夹中创建自己的文件夹，并写入文件是不需要权限的
     * android 10 没权限执行成功
     * android 8 成功
     */
    private fun safCreateDirAndFile() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                99
//            )
//            Toast.makeText(this, "先授予存储权限", Toast.LENGTH_SHORT).show()
//            return
//        }
        // 直接用过用户 授予权限方式 不需要权限
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            }
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        startActivityForResult(intent, REQ_SAF_CHOOSE_DIR)
    }

    private fun chooseImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Toast.makeText(this, "need KITKAT", Toast.LENGTH_SHORT).show()
            return
        }
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).also {
            it.type = "image/*"
        }, REQ_CHOOSE_IMG)
    }

    /**
     * 加载一张图片太麻烦了，要先查询图片的uri，根据uri读取
     * 既是必须先查询一次，才晓得uri
     * 老的方式，晓得文件就可以读取了
     */
    private fun loadJustCreateImage() {
        // 先查询uri
        val imageUri: Uri? = findImageUri() ?: return
        loadImageFromUri(imageUri, img_1)
    }

    /**
     * [MediaStore.Images.Media.TITLE] 试了不行
     */
    private fun findImageUri(): Uri? {
        var imageUri1: Uri? = null
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.DISPLAY_NAME + "=?"
        val args = arrayOf(image_name)
        val projection =
            arrayOf(MediaStore.Images.Media._ID)
        val cursor: Cursor? = contentResolver.query(external, projection, selection, args, null)
        if (cursor != null && cursor.moveToFirst()) {
            imageUri1 = ContentUris.withAppendedId(external, cursor.getLong(0))
            cursor.close()
        }
        return imageUri1
    }

    /**
     * 在 9.0上就可以运行
     * 1. 不需要存储权限可以创建文件
     * 2. 访问其他非自己创建文件需要权限
     * 3. 若以创建的文件被删除了，再次创建会无法创建成功，奇葩
     * 4. 若文件已存在，则不覆盖，重新创建新的名称文件，本来是Image.png 后面就成了Image（1）.png
     * 在 9.0 之前
     * 1. 需要存储权限
     * 2. 而且不能自定义目录 比如：Pictures/test
     * 3. 生成的文件不是按照给定的名称，既：不能指定名称
     * 在公共目录创建文件
     */
    private fun assessPublicImage() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, image_name)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.TITLE, "Image.png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 路径不对也会崩溃，比如这里使用Download/test
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test")
        }

        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        /*
        冲突处理机制，如果已经存在，先删除
        如果不删，会创建新的重命名文件，而不是覆盖，而且也没有提供选项来如何处理冲突。
         */
        findImageUri()?.let {
            contentResolver.delete(it, null, null)
            Toast.makeText(this, "suc delete old image", Toast.LENGTH_SHORT).show()
        }

        val insertUri = contentResolver.insert(external, values) ?: error("error insert")
        Log.i(TAG, "insertUri: $insertUri")


        var os: OutputStream? = null
        try {
            os = contentResolver.openOutputStream(insertUri) ?: error(" query uri")
//            val bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, os)
            // write what you want
        } catch (e: IOException) {
            Toast.makeText(this, "无法保存图片", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "fail: " + e.cause)
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                Log.e(TAG, "fail in close: " + e.cause)
            }
        }

    }

    /**
     * 这里访问的是 sdcard/Download 不是 Downloads
     */
    private fun assessPublicDocument() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(this, "need Q", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues()
        values.put(MediaStore.Downloads.DISPLAY_NAME, "Image2.txt")
        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        values.put(MediaStore.Downloads.TITLE, "Image.png")
        values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/test")

        val external = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        val insertUri = contentResolver.insert(external, values) ?: error("error insert")
        Log.i(TAG, "insertUri: $insertUri")

        val os: OutputStream? = contentResolver.openOutputStream(insertUri) ?: error(" query uri")
        os?.use {
            it.write("hello the fuck scopedStorage".toByteArray())
        }
    }

    /**
     * 直接访问App 不需要任何权限，不受 ScopedStorage 限制
     * 自己的目录 sdcard/Android/data/pkg/files
     */
    private fun accessAppSpec() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            throw RuntimeException("${Build.VERSION_CODES.KITKAT}")
        }
        // set "Documents" as subDir
        val dirs = getExternalFilesDirs("Documents")
        val primaryDir = dirs?.get(0) ?: return
        var fileOS: OutputStream? = null
        try {
            fileOS = FileOutputStream(File(primaryDir.absolutePath, "MyTestDocument.txt"))
            fileOS.write("file is created".toByteArray(StandardCharsets.UTF_8))
            fileOS.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "create file fail")
            Toast.makeText(this, "无法访问内部文件", Toast.LENGTH_SHORT).show()
        } finally {
            try {
                fileOS?.close()
            } catch (e1: IOException) {
                Log.e(TAG, "close stream fail")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createDir(intent: Intent?) {
        Log.d(
            TAG,
            String.format("Open Directory result Uri : %s", intent?.data)
        )
        // Q 上并不能创建 java.io.FileNotFoundException: Directory creation not supported
        intent?.data?.let { uri ->
            printUri(uri)
            printChildUri(uri)

            val docUri = DocumentsContract.buildDocumentUriUsingTree(
                uri,
                DocumentsContract.getTreeDocumentId(uri)
            )
            val resultUri = default {
                DocumentsContract.createDocument(
                    contentResolver,
                    docUri,
                    DocumentsContract.Document.MIME_TYPE_DIR,
                    "mock"
                )
            }
            val suc = null != resultUri

            Toast.makeText(this, "dir create? $suc", Toast.LENGTH_SHORT).show()

            Log.e(TAG, "make dir ${resultUri?.toString()} ,suc?$suc")

        } ?: return
    }

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun printChildUri(uri: Uri) {
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        val childCursor = contentResolver.query(
            childrenUri, arrayOf(
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE
            ), null, null, null
        ) ?: return
        childCursor.use { cursor ->
            while (cursor.moveToNext()) {
                Log.d(
                    TAG,
                    "found child=" + cursor.getString(0) + ", mime=" + cursor
                        .getString(1)
                )
            }
        }
    }

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun printUri(uri: Uri) {
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )
        val docCursor = contentResolver.query(
            docUri, arrayOf(
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_FLAGS
            ), null, null, null
        ) ?: return
        docCursor.use { cursor ->
            while (cursor.moveToNext()) {
                Log.d(
                    TAG,
                    "found doc =" + cursor.getString(0) + ", mime=" + cursor
                        .getString(1) + ",flags=${cursor.getInt(2)}"
                )
            }
        }
    }

    /**
     * 尝试在 sdcard 创建 mock 文件夹
     */
    private fun justAccessSd() {
        val exist = Environment.getExternalStorageDirectory().exists()
        Log.e(TAG, "getExternalStorageDirectory exist?$exist")
        if (oldWay()) {
            // 虽然API是29的，但是运行在之前的手机上还是需要使用老方式
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val mockDir = File(Environment.getExternalStorageDirectory(), "mock")
                val mkdirResult = mockDir.mkdir()
                Log.e(TAG, "make dir ${mockDir.absolutePath} ,suc?$mkdirResult")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    99
                )
                Toast.makeText(this, "先授予存储权限", Toast.LENGTH_SHORT).show()
            }
            return
        }
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, REQ_CREATE_DIR)

        // 实际上，是无法获得根目录的，所以就无法直接使用
//        val resultUri = default {
//            DocumentsContract.createDocument(
//                contentResolver,
//                Uri.parse(Environment.getExternalStorageDirectory().absolutePath),
//                DocumentsContract.Document.MIME_TYPE_DIR,
//                "mock"
//            )
//        }
//
//        Log.e(TAG, "make dir ${resultUri?.toString()} ,suc?${null != resultUri}")
    }
}