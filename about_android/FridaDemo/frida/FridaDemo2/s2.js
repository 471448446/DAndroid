console.log("start...")
Java.perform(function intercapt() {
    var targetPath = "com.better.learn.fridademo.FridaDemo2Activity";
    var my_class = Java.use(targetPath);
    var string_class = Java.use("java.lang.String");
    //  hook overload method
    my_class.fun.overload("int", "int").implementation = function (x, y) {
        console.log("origin call: fun(" + x + "," + y + ")" + "intercept: fun(2,3)");
        var result = this.fun(2, 3);
        return result;
    }
    // 之前是没有加overload()
    my_class.fun.overload("java.lang.String").implementation = function (x) {
        console.log("origin call: fun(" + x + ")" + "intercapt:fun(" + x + "!!!" + ")")
        // create Object $new()
        var new_string = string_class.$new("!!!");
        var result = this.fun(x + new_string);
        return result;
    }
    // 如果要调用 secret()方法，是无法像上面那么直接写，因为它并没有在OnCreate()中被调用
    // 所以解决这种方案是，在内存中寻找已经存在的FridaDemo2Activity对象，并调用它的方法
    // 已运行就调用 secret() 时机上不是很合理
    // Java.choose 查找某个类的所有实例
    Java.choose(targetPath, {
        onMatch: function (instance) {
            console.log("found insatnce" + instance);
            console.log("invoke secret():" + instance.secret());
        },
        onComplete: function () {

        }
    });
});