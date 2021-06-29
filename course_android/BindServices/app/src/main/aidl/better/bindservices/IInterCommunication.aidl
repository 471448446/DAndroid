// IInterCommunication.aidl
package better.bindservices;

import better.bindservices.data.MyMessage;
import better.bindservices.ILocCallBack;
// Declare any non-default types here with import statements

interface IInterCommunication {
//    /**
//     * Demonstrates some basic types that you can use as parameters
//     * and return values in AIDL.
//     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
      double getLat();

      void sendYouAMessage(in String msg);
      void sendInt(in int length);

      void sendMyMessage(in MyMessage msg);
      /**
      * 测试 in out inout
      * */
      MyMessage messageIn(in MyMessage msg);
      MyMessage messageOut(out MyMessage msg);
      MyMessage messageInOut(inout MyMessage msg);
      /**
      * 服务端主动与客户端通讯
      */
      void registerCallBack(in ILocCallBack callBack);
      void unRegisterCallBack(in ILocCallBack callBack);
      /**
      * 如果remote 崩溃，客户端会怎样
      */
      int justCrash();
}
