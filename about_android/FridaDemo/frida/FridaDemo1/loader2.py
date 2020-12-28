import frida, sys
# https://frida.re/docs/examples/android/
# 先运行程序
def on_message(message, data):
    if message['type'] == 'send':
        print("[*] {0}".format(message['payload']))
    else:
        print(message)

jscode = """
console.log("Script loaded successfully ");
Java.perform(function x() { //Silently fails without the sleep from the python code
    console.log("Inside java perform function");
    //get a wrapper for our class
    var my_class = Java.use("com.better.learn.fridademo.FridaDemo1Activity");
    //replace the original implmenetation of the function `fun` with our custom function
    console.log(my_class)
    my_class.fun.implementation = function (x, y) {
        //print the original arguments
        console.log("original call: fun(" + x + ", " + y + ")");
        //call the original implementation of `fun` with args (2,5)
        var ret_value = this.fun(2, 5);
        return ret_value;
    }
});
"""

process = frida.get_usb_device().attach('com.better.learn.fridademo')
script = process.create_script(jscode)
script.on('message', on_message)
print('[*] Running CTF')
script.load()
sys.stdin.read()