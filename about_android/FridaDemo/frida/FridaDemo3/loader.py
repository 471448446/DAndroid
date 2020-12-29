import frida


def on_message(message, data):
    print(message)
    print(data)


process = frida.get_usb_device().attach("com.better.learn.fridademo")
with open("s3_2.js", 'r', encoding='utf-8') as f:
    script = process.create_script(f.read())
script.on('message', on_message)
script.load()
# input()

command = ""
while 1 == 1:
    command = input(
        "Enter command:\n1: Exit\n2: Call secret function\nchoice:")
    if command == "1":
        break
    elif command == "2":
        script.exports.callsercetfunction()
