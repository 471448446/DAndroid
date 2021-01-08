import frida


def on_message(msg, payload):
    if payload!=None:
        print("%s , %s" %(msg,payload))
    else:
        print(msg)

process = frida.get_usb_device().attach("com.better.learn.fridademo")
with open("s5.js", 'r', encoding='utf-8') as f:
    script = process.create_script(f.read())
script.on('message', on_message)
script.load()
input()
