var instances_arr = [];
var target_path = "com.better.learn.fridademo.FridaDemo3Activity"

Java.perform(function intercept() {
    var targer_class = Java.use(target_path);
    var string_class = Java.use("java.lang.String");
    targer_class.fun.overload("java.lang.String").implementation = function (x) {
        var tmp = string_class.$new("!!!");
        var result = this.fun(x + tmp)
        return result;
    }
});
/**
 * 若干找到了内存中的实例，就直接调用，而不是每次都去寻找
 * 所有的操作代码都必须在Java.perform() 函数中
 */
function callSercet() {
    Java.perform(function intercept() {
        if (instances_arr.length != 0) {
            var i;
            for (i = 0; i < instances_arr.length; i++) {
                callInstanceOfSercet(instances_arr[0])
            }
            return
        }
        Java.choose(target_path, {
            onMatch: function (instance) {
                console.log("found insatnce" + instance);
                instances_arr.push(instance)
                callInstanceOfSercet(instance)
            },
            onComplete: function () {

            }
        });
    });
}

function callInstanceOfSercet(instance) {
    console.log("invoke secret():" + instance.secret());
}

rpc.exports = {
    callsercetfunction: callSercet
};