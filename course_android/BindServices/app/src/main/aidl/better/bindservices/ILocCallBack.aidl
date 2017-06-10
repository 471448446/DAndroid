// ILocCallBack.aidl
package better.bindservices;

// Declare any non-default types here with import statements
/**
* 服务端告知向客户端
*/
interface ILocCallBack {
    void locSuccess();
    void locFail();
}
