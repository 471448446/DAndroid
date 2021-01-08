// 如何强制转换对象: Java.cast(object,class)
// 调用对象
Java.perform(function () {
    send("start...");
    var string_class = Java.use("java.lang.String");
    var secereKey_class = Java.use("javax.crypto.spec.SecretKeySpec");
    secereKey_class.$init.overload("[B", "java.lang.String").implementation = function (x, y) {
        send("key:" + string_class.$new(x));
        // 一种发送数据方式，将真实的数据放在 plyload中
        send('{"my_type":"KEY"}', new Uint8Array(x));
        return this.$init(x, y);
    }
    var gen_class = Java.use("javax.crypto.spec.IvParameterSpec");
    gen_class.$init.overload("[B").implementation = function (x) {
        send('{"my_type":"GEN"}', new Uint8Array(x));
        return this.$init(x);
    }

    var cipher_class = Java.use("javax.crypto.Cipher");
    //now we will hook init function in class Cipher, we will be able to tie keys,IVs with Cipher objects
    // 直接打印函数参数
    cipher_class.init.overload("int", "java.security.Key", "java.security.spec.AlgorithmParameterSpec").implementation = function (x, y, z) {
        //console.log(z.getClass()); 
        if (x == 1) // 1 means Cipher.MODE_ENCRYPT
            send('{"my_type" : "hashcode_enc", "hashcode" :"' + this.hashCode().toString() + '" }');
        else // In this android app it is either 1 (Cipher.MODE_ENCRYPT) or 2 (Cipher.MODE_DECRYPT)
            send('{"my_type" : "hashcode_dec", "hashcode" :"' + this.hashCode().toString() + '" }');
        //We will have two lists in the python code, which keep track of the Cipher objects and their modes.

        //Also we can obtain the key,iv from the args passed to init call
        send('{"my_type" : "Key from call to cipher init"}', new Uint8Array(y.getEncoded()));
        //arg z is of type AlgorithmParameterSpec, we need to cast it to IvParameterSpec first to be able to call getIV function
        send('{"my_type" : "IV from call to cipher init"}', new Uint8Array(Java.cast(z, gen_class).getIV()));
        //init must be called this way to work properly
        // 新方式？？？
        return cipher_class.init.overload("int", "java.security.Key", "java.security.spec.AlgorithmParameterSpec").call(this, x, y, z);
        // 一样
        // return this.init(x, y, z);
    }
    cipher_class.doFinal.overload("[B").implementation = function (x) {
        send('{"my_type" : "before_doFinal" , "hashcode" :"' + this.hashCode().toString() + '" }', new Uint8Array(x));
        var ret = cipher_class.doFinal.overload("[B").call(this, x);
        send('{"my_type" : "after_doFinal" , "hashcode" :"' + this.hashCode().toString() + '" }', new Uint8Array(ret));

        return ret;
    }
});