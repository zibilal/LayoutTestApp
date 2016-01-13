package com.zibilal.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bilal on 1/13/2016.
 */
public class AuthRequest extends StringRequest {
    private String mToken;
    private String mParams;

    public AuthRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public AuthRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public void setToken(String token) {
        mToken = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + "JlPDQ7fY4ngkzqiVwGENjwNPLCLOQ6P2veM7MwtL4zk1TJIClvFoaUKSTIBy4tUQjZ51Ayz8dD0B6nERiYPDPAXe-ijqaNHyyCvIzVnIsc0WKPPUdU77T3ykIfri1oTJW5mONJCRRalE8HiurILboM2Hi71x0X3JG5UOPLU9HBrXH_osZCzCf9eNI5c8rjM8-wFx3bPdQUt557JBc6Tpnjp8YJqH5PhpxcEaa1jiI12pkw7fvIvQ6uJZEDocZr8ccfNIleCTk_dqjLtE09fE2V8nkv7VduiKwkqdwEcB0jN_ffP1ikKEMhlFWS4t4t5uIbSk7ggOFJXgXG83Sl__I0XpfVf8jN9AsiyEjFdEpeoGXYWu4HkaDxYCb9oWMJACmMzZgKaaC0PiXom8Pt_QbBV9yVXjJjafCwzqjov9WvOH_baeCaCBPaQ4Tx-gC7yF9XmXokicZWT8MxLSTZVxYIzYmr28ESkbgC9mLN59ZzXgO-xbKy9Zh25nIMkdsd3O");
        return headerMap;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    public void setParams(String json) {
        mParams = json;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mParams.getBytes();
    }
}
