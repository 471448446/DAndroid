// ILocCallBack.aidl
package better.bindservices;

import better.bindservices.data.MyMessage;

// Declare any non-default types here with import statements
/**
* 服务端告知向客户端
*/
interface ILocCallBack {
    void locSuccess();
    void locFail();
    MyMessage callMessageOut(out MyMessage msg);
    MyMessage callMessageIn(in MyMessage msg);
    MyMessage callMessageInOut(inout MyMessage msg);
}
