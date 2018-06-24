package com.grayseagull.edgar.livedesign;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RenderClass implements GLSurfaceView.Renderer {

    private Cuadro lienzo;
    private Context mContent;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private float[] mRotationMatrix = new float[16];
    //private float[] mScaleMatrix = new float[16];

    public volatile float mAngle;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    //public float mScaleFactor=1.f;

    //public float getScaleFactor() {        return mScaleFactor;    }

    //public void setScaleFactor(float ScaleFactor) {        mScaleFactor = ScaleFactor;    }



    public RenderClass (Context content){
        mContent = content;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor( 0f, 0f, 0f, 0f );
        lienzo = new Cuadro(mContent);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport( 0, 0, width, height );

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];

        GLES20.glClear( GL10.GL_COLOR_BUFFER_BIT );

        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
        //Matrix.scaleM(mScaleMatrix,0,mScaleFactor,mScaleFactor,-1.0f);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mScaleMatrix, 0);

        lienzo.draw(scratch);
    }
}
