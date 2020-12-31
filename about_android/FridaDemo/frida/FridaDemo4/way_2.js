console.log("script load successfuly");
Java.perform(function () {
    var string_class = Java.use("java.lang.String");
    // Fail 这是一个 native 方法。有点不一样
    string_class.compareTo.overload("java.lang.String").implementation = function (x) {
        print("compare:" + x)
        var data = x.toString();
        if ("admin" == data) {
            return 1;
        } else {
            return this.compare(data);
        }
    }
});