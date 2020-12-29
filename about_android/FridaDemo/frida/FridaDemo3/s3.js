/**
 * 在demo2中调用secret()方法是在JS一被注册的时候就被调用了。
 * 这里定义了一个JS方法callSecret()，当外界想调用时，就调用这个方法就行
 */
function callSercet() {
    Java.perform(function intercept() {
        var target_path = "com.better.learn.fridademo.FridaDemo3Activity"
        var targer_class = Java.use(target_path);
        var string_class = Java.use("java.lang.String");
        targer_class.fun.overload("java.lang.String").implementation = function (x) {
            var tmp = string_class.$new("!!!");
            var result = this.fun(x + tmp)
            return result;
        }
        Java.choose(target_path, {
            onMatch: function (instance) {
                console.log("found insatnce" + instance);
                console.log("invoke secret():" + instance.secret());
            },
            onComplete: function () {

            }
        });
    });
}

rpc.exports = {
    // 将JS中定义的callSercet() 方法对外暴露为callsercetfunction
    // 对外暴露的方法名称，不能有大写，也不能有下划线
    callsercetfunction: callSercet
};