//package com.better.learn.gl20.fuck;
//
//import android.content.Context;
//import android.opengl.GLES20;
//
//import com.better.learn.gl20.R;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//import static android.opengl.GLES20.GL_TEXTURE0;
//import static android.opengl.GLES20.GL_TEXTURE_2D;
//import static android.opengl.GLES20.glActiveTexture;
//import static android.opengl.GLES20.glBindTexture;
//import static android.opengl.GLES20.glUniform1i;
//
///**
// * Created by better on 2020/3/14 6:39 PM.
// */
//public class CubeTexture extends BShape {
//    //顶点坐标
//    private FloatBuffer vertexBuffer;
//    private Context context;
//    //float类型的字节数
//    private static final int BYTES_PER_FLOAT = 4;
//    //共有72个顶点坐标，每个面包含12个顶点坐标
//    private static final int POSITION_COMPONENT_COUNT = 6 * 12;
//    // 数组中每个顶点的坐标数
//    private static final int COORDS_PER_VERTEX = 3;
//    // 颜色数组中每个颜色的值数
//
//    private static final String A_POSITION = "a_Position";
//    private static final String U_MATRIX = "u_Matrix";
//    private int uMatrixLocation;
//    private int aPositionLocation;
//    private int program;
//    private static final int TEXTURE_COORDIANTES_COMPONENT_COUNT = 2; //一个纹理坐标含有的元素个数
//    private static final int STRIDE = (COORDS_PER_VERTEX + TEXTURE_COORDIANTES_COMPONENT_COUNT)
//            * BYTES_PER_FLOAT;
//
//    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";//纹理
//    private static final String U_TEXTURE_UNIT = "u_TextureUnit";//纹理
//    private int uTextureUnitLocation;
//    private int aTextureCoordinates;
//    private int texture;
//
//    static float vertices[] = {
//            //X   Y   Z        S    T
//            //前面
//            0, 0, 1.0f, 0.5f, 0.5f,
//            1.0f, 1.0f, 1.0f, 1.0f, 0,
//            -1.0f, 1.0f, 1.0f, 0, 0,
//            0, 0, 1.0f, 0.5f, 0.5f,
//            -1.0f, 1.0f, 1.0f, 0, 0,
//            -1.0f, -1.0f, 1.0f, 0, 1.0f,
//            0, 0, 1.0f, 0.5f, 0.5f,
//            -1.0f, -1.0f, 1.0f, 0, 1.0f,
//            1.0f, -1.0f, 1.0f, 1.0f, 1.0f,
//            0, 0, 1.0f, 0.5f, 0.5f,
//            1.0f, -1.0f, 1.0f, 1.0f, 1.0f,
//            1.0f, 1.0f, 1.0f, 1.0f, 0,
//            //后面
//            0, 0, -1.0f, 0.5f, 0.5f,
//            1.0f, 1.0f, -1.0f, 1.0f, 0,
//            1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
//            0, 0, -1.0f, 0.5f, 0.5f,
//            1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
//            -1.0f, -1.0f, -1.0f, 0, 1.0f,
//            0, 0, -1.0f, 0.5f, 0.5f,
//            -1.0f, -1.0f, -1.0f, 0, 1.0f,
//            -1.0f, 1.0f, -1.0f, 0, 0,
//            0, 0, -1.0f, 0.5f, 0.5f,
//            -1.0f, 1.0f, -1.0f, 0, 0,
//            1.0f, 1.0f, -1.0f, 1.0f, 0,
//            //左面
//            -1.0f, 0, 0, 0.5f, 0.5f,
//            -1.0f, 1.0f, 1.0f, 1.0f, 0,
//            -1.0f, 1.0f, -1.0f, 0, 0,
//            -1.0f, 0, 0, 0.5f, 0.5f,
//            -1.0f, 1.0f, -1.0f, 0, 0,
//            -1.0f, -1.0f, -1.0f, 0, 1.0f,
//            -1.0f, 0, 0, 0.5f, 0.5f,
//            -1.0f, -1.0f, -1.0f, 0, 1.0f,
//            -1.0f, -1.0f, 1.0f, 1.0f, 1.0f,
//            -1.0f, 0, 0, 0.5f, 0.5f,
//            -1.0f, -1.0f, 1.0f, 1.0f, 1.0f,
//            -1.0f, 1.0f, 1.0f, 1.0f, 0,
//            //右面
//            1.0f, 0, 0, 0.5f, 0.5f,
//            1.0f, 1.0f, 1.0f, 0, 0,
//            1.0f, -1.0f, 1.0f, 0, 1.0f,
//            1.0f, 0, 0, 0.5f, 0.5f,
//            1.0f, -1.0f, 1.0f, 0, 1.0f,
//            1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
//            1.0f, 0, 0, 0.5f, 0.5f,
//            1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
//            1.0f, 1.0f, -1.0f, 1.0f, 0,
//            1.0f, 0, 0, 0.5f, 0.5f,
//            1.0f, 1.0f, -1.0f, 1.0f, 0,
//            1.0f, 1.0f, 1.0f, 0, 0,
//            //上面
//            0, 1.0f, 0, 0.5f, 0.5f,
//            1.0f, 1.0f, 1.0f, 1.0f, 0,
//            1.0f, 1.0f, -1.0f, 1.0f, 1.0f,
//            0, 1.0f, 0, 0.5f, 0.5f,
//            1.0f, 1.0f, -1.0f, 1.0f, 1.0f,
//            -1.0f, 1.0f, -1.0f, 0, 1.0f,
//            0, 1.0f, 0, 0.5f, 0.5f,
//            -1.0f, 1.0f, -1.0f, 0, 1.0f,
//            -1.0f, 1.0f, 1.0f, 0, 0,
//            0, 1.0f, 0, 0.5f, 0.5f,
//            -1.0f, 1.0f, 1.0f, 0, 0,
//            1.0f, 1.0f, 1.0f, 1.0f, 0,
//            //下面
//            0, -1.0f, 0, 0.5f, 0.5f,
//            1.0f, -1.0f, 1.0f, 1.0f, 0,
//            -1.0f, -1.0f, 1.0f, 0, 0,
//            0, -1.0f, 0, 0.5f, 0.5f,
//            -1.0f, -1.0f, 1.0f, 0, 0,
//            -1.0f, -1.0f, -1.0f, 0, 1.0f,
//            0, -1.0f, 0, 0.5f, 0.5f,
//            -1.0f, -1.0f, -1.0f, 0, 1.0f,
//            1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
//            0, -1.0f, 0, 0.5f, 0.5f,
//            1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
//            1.0f, -1.0f, 1.0f, 1.0f, 0,
//    };
//
//    public CubeTexture(Context context) {
//        super(context);
//    }
//
//    @Override
//    protected int getFragmentResourceId() {
//        return R.raw.shape_vertex_shader_texture;
//    }
//
//    @Override
//    protected int getVertexResourceId() {
//        return R.raw.shape_fragment_shader_texture;
//    }
//
//    @Override
//    protected void initializeShape() {
//        vertexBuffer = ByteBuffer
//                .allocateDirect(vertices.length * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        // 把坐标们加入FloatBuffer中
//        vertexBuffer.put(vertices);
//        // 设置buffer，从第一个坐标开始读
//        vertexBuffer.position(0);
//
//        getProgram();
////
////        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
////        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
//
//        aTextureCoordinates = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
//        uTextureUnitLocation = GLES20.glGetAttribLocation(program, U_TEXTURE_UNIT);
//        texture = TextureHelper.loadTexture(context, R.drawable.ss,false);
//        // Set the active texture unit to texture unit 0.
//        glActiveTexture(GL_TEXTURE0);
//        // Bind the texture to this unit.
//        glBindTexture(GL_TEXTURE_2D, texture);
//        // Tell the texture uniform sampler to use this texture in the shader by
//        // telling it to read from texture unit 0.
//        glUniform1i(uTextureUnitLocation, 0);
//
//        //---------传入顶点数据数据
//        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT, false, STRIDE, vertexBuffer);
//        GLES20.glEnableVertexAttribArray(aPositionLocation);
//        //设置从第二个元素开始读取，因为从第二个元素开始才是纹理坐标
//        vertexBuffer.position(COORDS_PER_VERTEX);
//        GLES20.glVertexAttribPointer(aTextureCoordinates, TEXTURE_COORDIANTES_COMPONENT_COUNT,
//                GLES20.GL_FLOAT, false, STRIDE, vertexBuffer);
//        GLES20.glEnableVertexAttribArray(aTextureCoordinates);
//
//    }
//
//    @Override
//    protected void drawShape() {
//        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MatrixState.getFinalMatrix(),0);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
//    }
//}
