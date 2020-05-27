package com.genie.profileimageview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import java.lang.Exception

@SuppressLint("Recycle")
class ProfileImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var borderWidth = 0
    private var viewWidth = 0
    private var viewHeight = 0
    private var image: Bitmap? = null
    private var paint: Paint? = null
    private var paintBorder: Paint? = null
    private var shader: BitmapShader? = null
    private var shape: String = "circle"
    private var borderColor: Int = 0
    private var borderSize: Int = 0
    private var roundedCorner: Float = 0F
    private var ovalCorner: Float = 0F

    companion object{
        const val CIRCLE = "Circle"
        const val SQUARE = "Square"
        const val OVAL = "Oval"
        const val ROUNDED_CORNER = "Rounded Corner"
    }

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.isAntiAlias = true
        paintBorder = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBorder?.isAntiAlias = true
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder)
        paintBorder?.setShadowLayer(4.0f, 0.0f, 2.0f, Color.WHITE)

        if(attrs != null)
            getAttributeFormXml(attrs)
    }

    private fun getAttributeFormXml(attrs: AttributeSet?){
        try {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileImage)
            shape = typeArray.getString(R.styleable.ProfileImage_shape)!!
            borderColor = typeArray.getColor(R.styleable.ProfileImage_borderColor, 0)
            borderSize = typeArray.getInteger(R.styleable.ProfileImage_borderSize, 0)
            roundedCorner = typeArray.getInteger(R.styleable.ProfileImage_roundedCorner, 0).toFloat()
            ovalCorner = typeArray.getInteger(R.styleable.ProfileImage_ovalCorner, 0).toFloat()

            if(borderSize != 0){
                borderWidth = borderSize
            }

            if(borderColor != 0){
                setBorderColor(borderColor)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setBorderColor(borderColor: Int) {
        if (paintBorder != null)
            paintBorder?.color = borderColor
        this.invalidate()
    }

    private fun loadBitmap(){
        val bitmapDrawable = this.drawable

        if (bitmapDrawable != null)
            image = bitmapDrawable.toBitmap()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        loadBitmap()

        //To remove set Image from ImageView xml
        this.setImageDrawable(null)

        if (image != null) {
            shader = BitmapShader(Bitmap.createScaledBitmap(image!!, width, height, false),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint?.shader = shader

            when {
                shape.contains(CIRCLE, ignoreCase = true) -> {
                    //For Border Circular image
                    val circleCenter = viewWidth / 2
                    canvas?.drawCircle(circleCenter.toFloat() + borderWidth, circleCenter.toFloat() + borderWidth,
                        circleCenter + borderWidth - 4.0f, paintBorder!!)
                    canvas?.drawCircle(circleCenter.toFloat() + borderWidth, circleCenter.toFloat() + borderWidth,
                        circleCenter - 4.0f, paint!!)
                }
                shape.contains(SQUARE, ignoreCase = true) -> {
                    //For Square image
                    val rectBorder = RectF(0.0f, 0.0f,
                        width.toFloat() + borderWidth, height.toFloat() + borderWidth)
                    canvas?.drawRect(rectBorder, paintBorder!!)

                    val rect = RectF(0.0f + borderWidth, 0.0f + borderWidth,
                        width.toFloat() - borderWidth, height.toFloat() - borderWidth)
                    canvas?.drawRect(rect, paint!!)
                }
                shape.contains(OVAL, ignoreCase = true) -> {
                    //For Oval image
                    val rectBorder = RectF(0.0f + ovalCorner, 0.0f,
                        width.toFloat() + borderWidth - ovalCorner, height.toFloat() + borderWidth)
                    canvas?.drawOval(rectBorder, paintBorder!!)

                    val rect = RectF(0.0f + ovalCorner + borderWidth, 0.0f + borderWidth,
                        width.toFloat() - borderWidth  - ovalCorner, height.toFloat() - borderWidth)
                    canvas?.drawOval(rect, paint!!)
                }
                shape.contains(ROUNDED_CORNER, ignoreCase = true) -> {
                    //For Oval image
                    val rectBorder = RectF(0.0f, 0.0f, width.toFloat() + borderWidth, height.toFloat() + borderWidth)
                    canvas?.drawRoundRect(rectBorder, roundedCorner, roundedCorner, paintBorder!!)

                    val rect = RectF(0.0f + borderWidth, 0.0f + borderWidth,
                        width.toFloat() - borderWidth, height.toFloat() - borderWidth)
                    canvas?.drawRoundRect(rect, roundedCorner, roundedCorner, paint!!)
                }
                else -> {
                    //For Border Circular image
                    val circleCenter = viewWidth / 2
                    canvas?.drawCircle(circleCenter.toFloat() + borderWidth, circleCenter.toFloat() + borderWidth,
                        circleCenter + borderWidth - 4.0f, paintBorder!!)
                    canvas?.drawCircle(circleCenter.toFloat() + borderWidth, circleCenter.toFloat() + borderWidth,
                        circleCenter - 4.0f, paint!!)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)

        viewWidth = width - (borderWidth * 2)
        viewHeight = height - (borderWidth * 2)

        setMeasuredDimension(width, height)

    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = viewWidth
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = viewHeight
        }

        return (result + 2)
    }
}