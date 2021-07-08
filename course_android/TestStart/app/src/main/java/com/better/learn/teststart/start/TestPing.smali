.class public Lcom/better/learn/teststart/TestPing;
.super Ljava/lang/Object;
.source "TestPing.java"


# direct methods
.method public constructor <init>()V
    .registers 1

    .prologue
    .line 13
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static copy(Landroid/content/Context;Ljava/io/File;)V
    .registers 16
    .param p0, "context"    # Landroid/content/Context;
    .param p1, "file"    # Ljava/io/File;

    .prologue
    .line 33
    new-instance v10, Ljava/io/File;

    invoke-virtual {p0}, Landroid/content/Context;->getExternalCacheDir()Ljava/io/File;

    move-result-object v12

    const-string v13, "init"

    invoke-direct {v10, v12, v13}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    .line 34
    .local v10, "parent":Ljava/io/File;
    invoke-virtual {v10}, Ljava/io/File;->exists()Z

    move-result v12

    if-nez v12, :cond_14

    .line 35
    invoke-virtual {v10}, Ljava/io/File;->mkdir()Z

    .line 37
    :cond_14
    new-instance v5, Ljava/io/File;

    invoke-virtual {p1}, Ljava/io/File;->getName()Ljava/lang/String;

    move-result-object v12

    invoke-direct {v5, v10, v12}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    .line 38
    .local v5, "outFile":Ljava/io/File;
    const/4 v3, 0x0

    .line 39
    .local v3, "inputStream":Ljava/io/InputStream;
    const/4 v6, 0x0

    .line 41
    .local v6, "outputStream":Ljava/io/OutputStream;
    :try_start_1f
    invoke-virtual {v5}, Ljava/io/File;->exists()Z

    move-result v12

    if-nez v12, :cond_28

    .line 42
    invoke-virtual {v5}, Ljava/io/File;->createNewFile()Z

    .line 45
    :cond_28
    new-instance v4, Ljava/io/FileInputStream;

    invoke-direct {v4, p1}, Ljava/io/FileInputStream;-><init>(Ljava/io/File;)V
    :try_end_2d
    .catch Ljava/lang/Exception; {:try_start_1f .. :try_end_2d} :catch_6d
    .catchall {:try_start_1f .. :try_end_2d} :catchall_82

    .line 46
    .end local v3    # "inputStream":Ljava/io/InputStream;
    .local v4, "inputStream":Ljava/io/InputStream;
    :try_start_2d
    new-instance v7, Ljava/io/FileOutputStream;

    invoke-direct {v7, v5}, Ljava/io/FileOutputStream;-><init>(Ljava/io/File;)V
    :try_end_32
    .catch Ljava/lang/Exception; {:try_start_2d .. :try_end_32} :catch_9b
    .catchall {:try_start_2d .. :try_end_32} :catchall_94

    .line 48
    .end local v6    # "outputStream":Ljava/io/OutputStream;
    .local v7, "outputStream":Ljava/io/OutputStream;
    const/16 v12, 0x2000

    :try_start_34
    new-array v0, v12, [B

    .line 49
    .local v0, "buf":[B
    const/16 v1, 0x2000

    .line 50
    .local v1, "bufferReadLimit":I
    const-wide/16 v8, 0x0

    .line 53
    .local v8, "pBytesLeftToRead":J
    :goto_3a
    const/4 v12, 0x0

    const/16 v13, 0x2000

    invoke-virtual {v4, v0, v12, v13}, Ljava/io/InputStream;->read([BII)I

    move-result v11

    .local v11, "read":I
    const/4 v12, -0x1

    if-eq v11, v12, :cond_55

    .line 54
    int-to-long v12, v11

    cmp-long v12, v8, v12

    if-lez v12, :cond_50

    .line 55
    const/4 v12, 0x0

    invoke-virtual {v7, v0, v12, v11}, Ljava/io/OutputStream;->write([BII)V

    .line 56
    int-to-long v12, v11

    sub-long/2addr v8, v12

    goto :goto_3a

    .line 58
    :cond_50
    const/4 v12, 0x0

    long-to-int v13, v8

    invoke-virtual {v7, v0, v12, v13}, Ljava/io/OutputStream;->write([BII)V

    .line 62
    :cond_55
    invoke-virtual {v7}, Ljava/io/OutputStream;->flush()V
    :try_end_58
    .catch Ljava/lang/Exception; {:try_start_34 .. :try_end_58} :catch_9e
    .catchall {:try_start_34 .. :try_end_58} :catchall_97

    .line 67
    :try_start_58
    invoke-virtual {v4}, Ljava/io/InputStream;->close()V
    :try_end_5b
    .catch Ljava/lang/Throwable; {:try_start_58 .. :try_end_5b} :catch_61

    .line 72
    :goto_5b
    :try_start_5b
    invoke-virtual {v7}, Ljava/io/OutputStream;->close()V
    :try_end_5e
    .catch Ljava/lang/Throwable; {:try_start_5b .. :try_end_5e} :catch_66

    move-object v6, v7

    .end local v7    # "outputStream":Ljava/io/OutputStream;
    .restart local v6    # "outputStream":Ljava/io/OutputStream;
    move-object v3, v4

    .line 77
    .end local v0    # "buf":[B
    .end local v1    # "bufferReadLimit":I
    .end local v4    # "inputStream":Ljava/io/InputStream;
    .end local v8    # "pBytesLeftToRead":J
    .end local v11    # "read":I
    .restart local v3    # "inputStream":Ljava/io/InputStream;
    :goto_60
    return-void

    .line 68
    .end local v3    # "inputStream":Ljava/io/InputStream;
    .end local v6    # "outputStream":Ljava/io/OutputStream;
    .restart local v0    # "buf":[B
    .restart local v1    # "bufferReadLimit":I
    .restart local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v7    # "outputStream":Ljava/io/OutputStream;
    .restart local v8    # "pBytesLeftToRead":J
    .restart local v11    # "read":I
    :catch_61
    move-exception v2

    .line 69
    .local v2, "e":Ljava/lang/Throwable;
    invoke-virtual {v2}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_5b

    .line 73
    .end local v2    # "e":Ljava/lang/Throwable;
    :catch_66
    move-exception v2

    .line 74
    .restart local v2    # "e":Ljava/lang/Throwable;
    invoke-virtual {v2}, Ljava/lang/Throwable;->printStackTrace()V

    move-object v6, v7

    .end local v7    # "outputStream":Ljava/io/OutputStream;
    .restart local v6    # "outputStream":Ljava/io/OutputStream;
    move-object v3, v4

    .line 76
    .end local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v3    # "inputStream":Ljava/io/InputStream;
    goto :goto_60

    .line 63
    .end local v0    # "buf":[B
    .end local v1    # "bufferReadLimit":I
    .end local v2    # "e":Ljava/lang/Throwable;
    .end local v8    # "pBytesLeftToRead":J
    .end local v11    # "read":I
    :catch_6d
    move-exception v2

    .line 64
    .local v2, "e":Ljava/lang/Exception;
    :goto_6e
    :try_start_6e
    invoke-virtual {v2}, Ljava/lang/Exception;->printStackTrace()V
    :try_end_71
    .catchall {:try_start_6e .. :try_end_71} :catchall_82

    .line 67
    :try_start_71
    invoke-virtual {v3}, Ljava/io/InputStream;->close()V
    :try_end_74
    .catch Ljava/lang/Throwable; {:try_start_71 .. :try_end_74} :catch_7d

    .line 72
    .end local v2    # "e":Ljava/lang/Exception;
    :goto_74
    :try_start_74
    invoke-virtual {v6}, Ljava/io/OutputStream;->close()V
    :try_end_77
    .catch Ljava/lang/Throwable; {:try_start_74 .. :try_end_77} :catch_78

    goto :goto_60

    .line 73
    :catch_78
    move-exception v2

    .line 74
    .local v2, "e":Ljava/lang/Throwable;
    invoke-virtual {v2}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_60

    .line 68
    .local v2, "e":Ljava/lang/Exception;
    :catch_7d
    move-exception v2

    .line 69
    .local v2, "e":Ljava/lang/Throwable;
    invoke-virtual {v2}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_74

    .line 66
    .end local v2    # "e":Ljava/lang/Throwable;
    :catchall_82
    move-exception v12

    .line 67
    :goto_83
    :try_start_83
    invoke-virtual {v3}, Ljava/io/InputStream;->close()V
    :try_end_86
    .catch Ljava/lang/Throwable; {:try_start_83 .. :try_end_86} :catch_8a

    .line 72
    :goto_86
    :try_start_86
    invoke-virtual {v6}, Ljava/io/OutputStream;->close()V
    :try_end_89
    .catch Ljava/lang/Throwable; {:try_start_86 .. :try_end_89} :catch_8f

    .line 76
    :goto_89
    throw v12

    .line 68
    :catch_8a
    move-exception v2

    .line 69
    .restart local v2    # "e":Ljava/lang/Throwable;
    invoke-virtual {v2}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_86

    .line 73
    .end local v2    # "e":Ljava/lang/Throwable;
    :catch_8f
    move-exception v2

    .line 74
    .restart local v2    # "e":Ljava/lang/Throwable;
    invoke-virtual {v2}, Ljava/lang/Throwable;->printStackTrace()V

    goto :goto_89

    .line 66
    .end local v2    # "e":Ljava/lang/Throwable;
    .end local v3    # "inputStream":Ljava/io/InputStream;
    .restart local v4    # "inputStream":Ljava/io/InputStream;
    :catchall_94
    move-exception v12

    move-object v3, v4

    .end local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v3    # "inputStream":Ljava/io/InputStream;
    goto :goto_83

    .end local v3    # "inputStream":Ljava/io/InputStream;
    .end local v6    # "outputStream":Ljava/io/OutputStream;
    .restart local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v7    # "outputStream":Ljava/io/OutputStream;
    :catchall_97
    move-exception v12

    move-object v6, v7

    .end local v7    # "outputStream":Ljava/io/OutputStream;
    .restart local v6    # "outputStream":Ljava/io/OutputStream;
    move-object v3, v4

    .end local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v3    # "inputStream":Ljava/io/InputStream;
    goto :goto_83

    .line 63
    .end local v3    # "inputStream":Ljava/io/InputStream;
    .restart local v4    # "inputStream":Ljava/io/InputStream;
    :catch_9b
    move-exception v2

    move-object v3, v4

    .end local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v3    # "inputStream":Ljava/io/InputStream;
    goto :goto_6e

    .end local v3    # "inputStream":Ljava/io/InputStream;
    .end local v6    # "outputStream":Ljava/io/OutputStream;
    .restart local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v7    # "outputStream":Ljava/io/OutputStream;
    :catch_9e
    move-exception v2

    move-object v6, v7

    .end local v7    # "outputStream":Ljava/io/OutputStream;
    .restart local v6    # "outputStream":Ljava/io/OutputStream;
    move-object v3, v4

    .end local v4    # "inputStream":Ljava/io/InputStream;
    .restart local v3    # "inputStream":Ljava/io/InputStream;
    goto :goto_6e
.end method


# virtual methods
.method public alog()V
    .registers 3

    .prologue
    .line 16
    const-string v7, "Better"

    const-string v8, "prepare watch native"

    invoke-static {v7, v8}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 17
    const-string v7, "Better"

    const-string v8, "prepare watch service"

    invoke-static {v7, v8}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 18
    return-void
.end method

.method public test(Landroid/content/Context;Ljava/lang/String;)V
    .registers 8
    .param p1, "context"    # Landroid/content/Context;
    .param p2, "parent"    # Ljava/lang/String;

    .prologue
    .line 21
    new-instance v1, Ljava/io/File;

    invoke-direct {v1, p2}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 24
    .local v1, "v6":Ljava/io/File;
    invoke-static {p1, v1}, Lcom/better/learn/teststart/TestPing;->copy(Landroid/content/Context;Ljava/io/File;)V

    .line 27
    if-eqz v1, :cond_32

    const/4 v2, 0x1

    :goto_b
    invoke-static {v2}, Ljava/lang/String;->valueOf(Z)Ljava/lang/String;

    move-result-object v0

    .line 28
    .local v0, "b":Ljava/lang/String;
    const-string v2, "Better"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "main() kill decode :"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v2, v3}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 30
    return-void

    .line 27
    .end local v0    # "b":Ljava/lang/String;
    :cond_32
    const/4 v2, 0x0

    goto :goto_b
.end method
