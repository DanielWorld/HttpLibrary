package com.danielpark.httpconnection.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.danielpark.httpconnection.listener.ResponseHandlerInterface;
import com.danielpark.httpconnection.network.HttpStatus;
import com.danielpark.httpconnection.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URI;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONTokener;

/**
 * Copyright (c) 2014-2016 daniel@bapul.net
 * Created by Daniel Park on 2016-04-07.
 */
public class AsyncHttpResponseHandler implements ResponseHandlerInterface, Callback {

    private Logger LOG = Logger.getInstance();

//    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String UTF8_BOM = "\uFEFF";
    private Handler handler;
    private boolean useSynchronousMode;

    private Looper looper = null;

    public AsyncHttpResponseHandler(){
        this(null);
    }

    public AsyncHttpResponseHandler(Looper looper){
        this.looper = looper == null ? Looper.myLooper() : looper;

        // Use asynchronous mode by default
        setUseSynchronousMode(false);
    }


    @Override
    public boolean getUseSynchronousMode() {
        return useSynchronousMode;
    }

    @Override
    public void setUseSynchronousMode(boolean sync) {
        // A looper must be prepared before setting asynchrous mode.
        if (!sync && looper == null) {
            sync = true;
            LOG.w("Current thread has not called Looper.prepare(). Forcing synchronous mode.");
        }

        // If using asynchronous mode.
        if (!sync && handler == null) {
            // Create a handler on current thread to submit tasks
            handler = new Handler(looper);
        } else if (sync && handler != null) {
            // TODO: Consider adding a flag to remove all queued messages.
            handler = null;
        }

        useSynchronousMode = sync;
    }

    protected void postRunnable(Runnable runnable) {
        if (runnable != null) {
            if (getUseSynchronousMode() || handler == null) // or Sync mode
            {
                // This response handler is synchronous, run on current thread
                runnable.run();
            } else {
                // Otherwise, run on provided handler
                handler.post(runnable);
            }
        }
    }

    public void onSuccess(int statusCode, Headers headers, String response) {
        // Daniel (2016-04-08 15:21:18): return default arguments
    }

    public void onFailure(int statusCode, Headers headers, String response) {
        // Daniel (2016-04-11 18:58:50): return default arguments
    }

	@Override
	public void onResponse(final Call call, final Response response) throws IOException {
		if (response.isSuccessful()) {
			if (response.code() != HttpStatus.SC_NO_CONTENT) {
				Runnable parser = new Runnable() {
					@Override
					public void run() {
						try {
							final String jsonResponse = getResponseString(response.body().bytes());
							postRunnable(new Runnable() {
								@Override
								public void run() {
									if (jsonResponse == null)
										onSuccess(response.code(), response.headers(), "");
									else
										onSuccess(response.code(), response.headers(), jsonResponse);
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				try {
					if (!getUseSynchronousMode()) {
						// proceed in Async Http connection mode
						new Thread(parser).start();
					} else {
						// proceed in Sync Http connection mode
						parser.run();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Runnable parser = new Runnable() {
					@Override
					public void run() {
						try {
							postRunnable(new Runnable() {

								@Override
								public void run() {
									onSuccess(response.code(), response.headers(), "");
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								// Daniel (2016-06-20 18:30:42): https://square.github.io/okhttp/3.x/okhttp/okhttp3/ResponseBody.html
								// After calling response.body().bytes(), it automatically calls body().close()
								response.close();
							} catch (Exception e){
								e.printStackTrace();
							}
						}
					}
				};

				try {
					if (!getUseSynchronousMode()) {
						// proceed in Async Http connection mode
						new Thread(parser).start();
					} else {
						// proceed in Sync Http connection mode
						parser.run();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			Runnable parser = new Runnable() {
				@Override
				public void run() {
                    try {
                        final String jsonResponse = getResponseString(response.body().bytes());
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                if (jsonResponse == null)
                                    onFailure(response.code(), response.headers(), "");
                                else
                                    onFailure(response.code(), response.headers(), jsonResponse);
                            }
                        });
                    } catch (Exception e) {
						e.printStackTrace();
                    }
				}
			};

			try {
				if (!getUseSynchronousMode()) {
					// proceed in Async Http connection mode
					new Thread(parser).start();
				} else {
					// proceed in Sync Http connection mode
					parser.run();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onFailure(Call call, IOException io) {
		Runnable parser = new Runnable() {
			@Override
			public void run() {
				try {
					postRunnable(new Runnable() {

						@Override
						public void run() {
							onFailure(0, new Headers.Builder().build(), "");
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		try {
			if (!getUseSynchronousMode()) {
				// proceed in Async Http connection mode
				new Thread(parser).start();
			} else {
				// proceed in Sync Http connection mode
				parser.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Attempts to encode response bytes as string of set encoding
     *
     * @param stringBytes response bytes
     * @return String of set encoding or null
     */
    private String getResponseString(byte[] stringBytes) {
        try {
            String charset = "UTF-8";
            String toReturn = (stringBytes == null) ? null : new String(stringBytes, charset);
            if (toReturn != null && toReturn.startsWith(UTF8_BOM)) {
                return toReturn.substring(1);
            }
            return toReturn;
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
            return null;
        }
    }

}