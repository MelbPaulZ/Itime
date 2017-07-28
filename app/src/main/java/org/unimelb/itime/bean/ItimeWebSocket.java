package org.unimelb.itime.bean;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.annotation.NotNull;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Paul on 1/3/17.
 */

public class ItimeWebSocket {
    private static final String SERVER_URL = "https://timegenii.com";
    private static final String ITIME_MESSAGE = "itime_message";

    private String TAG = "ItimeWebSocket";
    private Socket socket;
    private Context context;

    private String userUid="";
    private String deviceUid="";
    private String jwsToken="";

    private Emitter.Listener onConnectListener, onDisconnectListener, onConnectErrorListener, itimeMessageListener;

    private static ItimeWebSocket instance;
    private OnConnectInterface onConnectInterface;

    private ItimeWebSocket(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ItimeWebSocket getInstance(Context context){
        if (instance==null){
            instance = new ItimeWebSocket(context);
        }
        return instance;
    }

    public void connect(){
        try {
            Log.i(TAG, "ItimeSocket: " + generateUrl());
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            instance.socket = IO.socket(generateUrl(), options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        instance.socket.connect();
        initConnectListeners();
        instance.socket.on(Socket.EVENT_CONNECT, onConnectListener);
        instance.socket.on(Socket.EVENT_DISCONNECT, onDisconnectListener);
        instance.socket.on(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener);
        instance.socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectErrorListener);
    }

    public void setOnItimeMessage(Emitter.Listener onItimeMessageListener){
        if (onItimeMessageListener == null){
            return;
        }
        this.itimeMessageListener = onItimeMessageListener;
        instance.socket.on(ITIME_MESSAGE, onItimeMessageListener);
    }

    public void disconnect(){
        instance.socket.disconnect();
        instance.socket.off(Socket.EVENT_CONNECT, onConnectListener);
        instance.socket.off(Socket.EVENT_DISCONNECT, onDisconnectListener);
        instance.socket.off(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener);
        instance.socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectErrorListener);
        if (itimeMessageListener!=null){
            instance.socket.off(ITIME_MESSAGE, itimeMessageListener);
        }

    }

    public void setUserUid(String userUid) {
        instance.userUid = userUid;
    }

    public void setDeviceUid(String deviceUid) {
        instance.deviceUid = deviceUid;
    }

    public void setJwsToken(String jwsToken) {
        instance.jwsToken = jwsToken;
    }

    private String generateUrl(){
        String base = SERVER_URL;
        return base + "?" + "user_uid=" + instance.userUid +
                "&" + "device_uid=" + instance.deviceUid +
                "&" + "jwt_token=" + instance.jwsToken;
    }

    /**
     *
     * @param onConnectInterface need to be called before connect, to set listeners
     */
    public void setOnConnectInterface(@NotNull OnConnectInterface onConnectInterface){
        this.onConnectInterface = onConnectInterface;
    }

    private void initConnectListeners(){
        onConnectListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (onConnectInterface!=null){
                    onConnectInterface.onConnect();
                }
            }
        };

        onDisconnectListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (onConnectInterface!=null){
                    onConnectInterface.onDisconnect();
                }
            }
        };

        onConnectErrorListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (onConnectInterface!=null){
                    onConnectInterface.onConnectError();
                }
            }
        };
    }

    public interface OnConnectInterface{
        void onConnect();
        void onConnectError();
        void onDisconnect();
    }

}

