/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wilder.fito

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.google.android.gms.vision.text.Text

import com.wilder.fito.camera.GraphicOverlay
import com.google.android.gms.vision.text.TextBlock

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class OcrGraphic internal constructor(overlay: GraphicOverlay<*>, val textBlock: List<Text>?, val textToFind: String, val hasSpace: Boolean) : GraphicOverlay.Graphic(overlay) {

    var id: Int = 0

     init {

        if (sRectPaint == null) {
            sRectPaint = Paint()
            sRectPaint!!.color = TEXT_COLOR
            sRectPaint!!.style = Paint.Style.STROKE
            sRectPaint!!.strokeWidth = 4.0f
        }

        if (sTextPaint == null) {
            sTextPaint = Paint()
            sTextPaint!!.color = TEXT_COLOR
            sTextPaint!!.textSize = 54.0f
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * *
     * @param y A y parameter in the relative context of the canvas.
     * *
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    override fun contains(x: Float, y: Float): Boolean {
        // TODO: Check if this graphic's text contains this point.
        return false
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas: Canvas) {
        // TODO: Draw the text onto the canvas.
        if (textBlock == null) {
            return
        }

        // Draws the bounding box around the TextBlock.
        for (component in textBlock) {

            val rect = RectF(component.boundingBox)
            val numberOfCharacters = component.value.length

            //TODO: Fix character size
            val characterSize = (rect.right-rect.left)/numberOfCharacters

            var leftRight: List<Float> = arrayListOf(0f, 0f)
            if (hasSpace){
                leftRight = trim(characterSize, component.value, textToFind)
            }

            rect.left = translateX(rect.left)//+leftRight[0])
            rect.top = translateY(rect.top)
            rect.right = translateX(rect.right)//-leftRight[1])
            rect.bottom = translateY(rect.bottom)
            canvas.drawRect(rect, sRectPaint!!)
        }

    }

    companion object {

        private val TEXT_COLOR = Color.WHITE

        private var sRectPaint: Paint? = null
        private var sTextPaint: Paint? = null
    }
}
