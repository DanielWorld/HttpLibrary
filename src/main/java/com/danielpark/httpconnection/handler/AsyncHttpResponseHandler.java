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

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String UTF8_BOM = "\uFEFF";
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int RETRY_MESSAGE = 5;
    protected static final int CANCEL_MESSAGE = 6;
    protected static final int BUFFER_SIZE = 4096;
    private static final String LOG_TAG = "AsyncHttpRH";
    private String responseCharset = DEFAULT_CHARSET;
    private Handler handler;
    private boolean useSynchronousMode;
    private boolean usePoolThread;

    private URI requestURI = null;
    private Headers requestHeaders = null;
    private Looper looper = null;
    private WeakReference<Object> TAG = new WeakReference<Object>(null);

    public AsyncHttpResponseHandler(){
        this(null);
    }

    public AsyncHttpResponseHandler(Looper looper){
        this.looper = looper == null ? Looper.myLooper() : looper;

        // Use asynchronous mode by default
        setUseSynchronousMode(false);

        // Do not use the pool's thread to fire callbacks by default.
        setUsePoolThread(false);
    }

    @Override
    public void sendResponseMessage(Response response) throws IOException {
        // do not process if request has been cancelled
        if (!Thread.currentThread().isInterrupted()) {

        }
    }

    @Override
    public void sendStartMessage() {

    }

    @Override
    public void sendFinishMessage() {

    }

    @Override
    public void sendProgressMessage(long bytesWritten, long bytesTotal) {

    }

    @Override
    public void sendCancelMessage() {

    }

    @Override
    public void sendSuccessMessage(int statusCode, Headers headers, byte[] responseBody) {

    }

    @Override
    public void sendFailureMessage(int statusCode, Headers headers, byte[] responseBody, Throwable error) {

    }

    @Override
    public void sendRetryMessage(int retryNo) {

    }

    @Override
    public URI getRequestURI() {
        return null;
    }

    @Override
    public void setRequestURI(URI requestURI) {

    }

    @Override
    public Headers getRequestHeaders() {
        return null;
    }

    @Override
    public void setRequestHeaders(Headers requestHeaders) {

    }

    @Override
    public boolean getUseSynchronousMode() {
        return useSynchronousMode;
    }

    @Override
    public boolean getUsePoolThread() {
        return false;
    }

    @Override
    public void setUsePoolThread(boolean pool) {
        // If pool thread is to be used, there's no point in keeping a reference
        // to the looper and no need for a handler.
        if (pool) {
            looper = null;
            handler = null;
        }

        usePoolThread = pool;
    }

    @Override
    public void onPreProcessResponse(ResponseHandlerInterface instance, Response response) {

    }

    @Override
    public void onPostProcessResponse(ResponseHandlerInterface instance, Response response) {

    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void setTag(Object TAG) {

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
						} catch (IOException e) {
							LOG.e(e.getMessage());
						}
					}
				};

				try {
					if (!getUseSynchronousMode() && !getUsePoolThread()) {
						// proceed in Async Http connection mode
						new Thread(parser).start();
					} else {
						// proceed in Sync Http connection mode
						parser.run();
					}
				} catch (Exception e) {
					LOG.e(e.getMessage());
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
							LOG.e(e.getMessage());
						}
					}
				};

				try {
					if (!getUseSynchronousMode() && !getUsePoolThread()) {
						// proceed in Async Http connection mode
						new Thread(parser).start();
					} else {
						// proceed in Sync Http connection mode
						parser.run();
					}
				} catch (Exception e) {
					LOG.e(e.getMessage());
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
                    } catch (IOException e) {
                        LOG.e(e.getMessage());
                    }
				}
			};

			try {
				if (!getUseSynchronousMode() && !getUsePoolThread()) {
					// proceed in Async Http connection mode
					new Thread(parser).start();
				} else {
					// proceed in Sync Http connection mode
					parser.run();
				}
			} catch (Exception e) {
				LOG.e(e.getMessage());
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
					LOG.e(e.getMessage());
				}
			}
		};

		try {
			if (!getUseSynchronousMode() && !getUsePoolThread()) {
				// proceed in Async Http connection mode
				new Thread(parser).start();
			} else {
				// proceed in Sync Http connection mode
				parser.run();
			}
		} catch (Exception e) {
			LOG.e(e.getMessage());
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
            LOG.e("Encoding response into string failed" + e.getMessage());
            return null;
        }
    }

}