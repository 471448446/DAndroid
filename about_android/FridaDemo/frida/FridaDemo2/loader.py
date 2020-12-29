import frida


def on_message(message, data):
    print(message)
    print(data)


process = frida.get_usb_device().attach('com.better.learn.fridademo')
with open("s2.js", 'r', encoding='utf-8') as f:
    script = process.create_script(f.read())
# register our handler to be called
# 接受来自于JS发出的错误信息
script.on('message', on_message)
script.load()
input()
