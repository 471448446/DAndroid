console.log("scripy load successfully");
Java.perform(function () {
    var textView_class = Java.use("android.widget.TextView");
    var string_class = Java.use("java.lang.String");
    textView_class.setText.overload("java.lang.CharSequence").implementation = function (x) {
        var string_to_send = x.toString();
        var string_modify;
        console.log("origin data:" + string_to_send);
        send(string_to_send); // 发送给python的信息
        recv(function (received_json_object) {
            string_modify = received_json_object.my_data;
            // 在这里转换类型也是不行的，原因未知
            // string_modify = string_class.$new(string_modify);
        }).wait();// 等待，直到有信息回传时
        // 这里源码是没有这一步的，但是没有这一步，始终提示我类型错误，使用typeof打印出来的类型是string，明明重载的就是String呀？？？
        // 后面将这个string使用Java类型种的String创建了新的对象，就ok了
        string_modify = string_class.$new(string_modify);// 只有在这才行，typeof 打印的类型是 object
        console.log(typeof string_modify);
        return this.setText(string_modify);
    }
});