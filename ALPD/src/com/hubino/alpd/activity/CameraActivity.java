package com.hubino.alpd.activity;


import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hubino.alpd.common.StringArraySerializer;

/**
* The Class CameraActivity.
* 
* @author Hubino
* @version 1.0.0 - The Class CameraActivity Created
* 
*/

@SuppressLint("NewApi") 
public class CameraActivity extends Activity implements OnClickListener {

	public final static String TAG = "CameraActivity";
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	private static int mImageCount = 0;
	private static Button mNextButton;
	
    private String NAMESPACE ;
    private String URL ;	
    private String SOAP_ACTION ;
    private String METHOD_NAME ;
    
    public static int commCount				= 0;
    private StringArraySerializer stringArray = null;
    private TextView mProcessText = null;
    private String[] images;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.camera_activity);
		
		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mNextButton = (Button) findViewById(R.id.button2);
		mNextButton.setOnClickListener(CameraActivity.this);
		mProcessText = (TextView) findViewById(R.id.fp_process);
		mProcessText.setVisibility(View.GONE);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG, "accountcamera onstop");
	}
	
	public void onClick(View arg0) {
		ResultActivity.message = null;
		Log.i(TAG, "accountcamera onClick");
		mImageCount = 0;
		
		stringArray = new StringArraySerializer();
		images = new String[commCount];
		camera.takePicture(null, null, photoCallback);
		inPreview = false;
		mNextButton.setEnabled(false);
	}

	@Override
	public void onResume() {
		Log.i(TAG, "accountcamera onResume");
		super.onResume();
		if (camera == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			Camera.CameraInfo info = new Camera.CameraInfo();

			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, info);

				if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					camera = Camera.open(i);
				}
			}
		}

		if (camera == null) {
			camera = Camera.open();
		}

		startPreview();
	}

	@Override
	public void onPause() {
		Log.i(TAG, "accountcamera onPause");
		if (inPreview) {
			camera.stopPreview();
		}

		camera.release();
		camera = null;
		inPreview = false;
		mImageCount = 0;

		super.onPause();
	}

	private void initPreview(int width, int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
				cameraConfigured = true;
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
				Toast.makeText(CameraActivity.this, t.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}
	
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			// no-op -- wait until surfaceChanged()
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			camera.setDisplayOrientation(90);
			initPreview(width, height);
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};

	/*
	 * This method captures the images and determines to pass whether to call entrollment OR authentication WS based on image count.
	 * It a Proof Of Concept method and we can enhance from here.
	 */
	
	Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			camera.startPreview();
			inPreview = true;
			
			if(mImageCount+1 == commCount)
				camera.stopPreview();
			
			Bitmap bitmapPicture= BitmapFactory.decodeByteArray(data, 0, data.length);
		    Matrix matrix = new Matrix();
		    matrix.postRotate(270);
		    int height=bitmapPicture.getHeight();
		    int width=bitmapPicture.getWidth();
		    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapPicture, 640, 480, true); // This image scalling has been done to use the same comman image for algorithms. 
		    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		    
		    ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        	byte[] byteArray = stream.toByteArray();
        	images[mImageCount] = Base64.encode(byteArray);
        	stringArray.add(images[mImageCount]);        	
        	mImageCount++;
        	
        	if(mImageCount == commCount-1) {
				mProcessText.setVisibility(View.VISIBLE);
			}
        	
			if (mImageCount < commCount) {
				camera.takePicture(null, null, photoCallback);
			}
			else if(mImageCount == commCount) {
				mProcessText.setVisibility(View.VISIBLE);
				Log.i(TAG, "*** Before WS call ***");
				NAMESPACE = "http://services.hubino.omar.com/";
				URL = "http://146.185.160.54:8080/ajmWeb/services/OmerWeb?wsdl";
				SOAP_ACTION = "androidService";
		    	METHOD_NAME = "androidService";
            	try{
	        		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	        		
	        		PropertyInfo stringArrayProperty = new PropertyInfo();
	        		stringArrayProperty.setName("images");
	        		stringArrayProperty.setValue(stringArray);
	        		stringArrayProperty.setType(stringArray.getClass());
	        		stringArrayProperty.setNamespace(NAMESPACE);
	    	        request.addProperty(stringArrayProperty);
	    	        
	    	        request.addProperty("userId", HomeActivity.userId);
	    	        if(commCount == 5) // Based on count, WS will be invoked
	    	        	request.addProperty("serviceType", "prepareMetadata");
					else
						request.addProperty("serviceType", "validateFace");
	        		
	    	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	        envelope.bodyOut = request;	    	    	        
	    	        envelope.setOutputSoapObject(request);
	    	        new MarshalBase64().register(envelope);   //serialization
	                envelope.encodingStyle = SoapEnvelope.ENC;
	    	        
	                HttpTransportSE httpTransport = new HttpTransportSE(URL);
	    	    	httpTransport.debug = true;
	    	    	
	    	        httpTransport.call(SOAP_ACTION, envelope);
	    	        Log.i(TAG, "requestDump:"+(String) httpTransport.requestDump);
	    	        Log.i(TAG, "responseDump:"+(String) httpTransport.responseDump);
	    	        Log.i(TAG, "*** After WS call ***");
	    	        
	    	        String errorCode = null;
	    	        String message = null;
	    	        
	    	        String result = (String) httpTransport.responseDump;	    	        
	    	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    	        factory.setNamespaceAware(true);
	    	        XmlPullParser parser = factory.newPullParser();
	    	        parser.setInput(new StringReader (result));
	    	        int eventType = parser.getEventType();
	    	        while (eventType != XmlPullParser.END_DOCUMENT) {
	    	        	if(eventType == XmlPullParser.START_TAG){
	    	        		if(parser.getName().equals("result")) {
	    	        			eventType = parser.next();
	    	        			if(eventType == XmlPullParser.TEXT)	{
	    	        				errorCode = parser.getText();
	    	        			}
	    	        		}
	    	        		else if(parser.getName().equals("message")) {
	    	        			eventType = parser.next();
	    	        			if(eventType == XmlPullParser.TEXT)	{
	    	        				message = parser.getText();
	    	        			}
	    	        		}
	    	        	}
	            	eventType = parser.next();
	    	        }
				
	    	        Log.i(TAG, "errorCode:"+errorCode);
	    	        Log.i(TAG, "message:"+message);
	    	        
	    	        /*
	    	         * This part is for managing the error handling 
	    	         * E00 is success then you can proceed further steps involved in the app.
	    	         * If E01 then display the message from WS
	    	         */
	    	        if(commCount == 5)
	    	        {
		    	        if(message.equals("E00"))
		    	        	ResultActivity.message="Enrollment Success";
		    	        else
		    	        	ResultActivity.message="Enrollment Failure";
	    	        }
	    	        else
	    	        {
	    	        	if(message.equals("E00"))
		    	        	ResultActivity.message="Authentication Success";
		    	        else
		    	        	ResultActivity.message="Authentication Failure";
	    	        }
	    	        
	    	        mProcessText.setVisibility(View.GONE);				
					camera.release();
					camera = null;
					inPreview = false;
            	}catch (Exception e) {
            		ResultActivity.message="Try Again";
            		Log.e(TAG, "throws an exception: " + e.getMessage());
    			} finally {
    				Intent intent = new Intent(CameraActivity.this, ResultActivity.class);
					startActivity(intent);
					finish();
    			}
			}
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
