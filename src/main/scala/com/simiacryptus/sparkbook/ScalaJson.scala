/*
 * Copyright (c) 2018 by Andrew Charneski.
 *
 * The author licenses this file to you under the
 * Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.simiacryptus.util.io

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import org.apache.commons.io.FileUtils
import javax.annotation.Nonnull
import javax.annotation.Nullable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.function.Supplier
import java.util.stream.IntStream

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.simiacryptus.sparkbook.Java8Util._

/**
  * The type Json util.
  */
object ScalaJson {
  /**
    * Get double array double [ ].
    *
    * @param array the array
    * @return the double [ ]
    */
  def getDoubleArray(@javax.annotation.Nonnull array: JsonArray): Array[Double] = IntStream.range(0, array.size).mapToDouble((i: Int) => array.get(i).getAsDouble).toArray

  /**
    * Get int array int [ ].
    *
    * @param array the array
    * @return the int [ ]
    */
  @Nullable def getIntArray(@Nullable array: JsonArray): Array[Int] = {
    if (null == array) return null
    IntStream.range(0, array.size).map((i: Int) => array.get(i).getAsInt).toArray
  }

  /**
    * Gets json.
    *
    * @param kernelDims the kernel dims
    * @return the json
    */
  @javax.annotation.Nonnull def getJson(@javax.annotation.Nonnull kernelDims: Array[Double]): JsonArray = {
    @javax.annotation.Nonnull val array = new JsonArray
    for (k <- kernelDims) {
      array.add(new JsonPrimitive(k))
    }
    array
  }

  @javax.annotation.Nonnull def getJson(@javax.annotation.Nonnull kernelDims: Array[Int]): JsonArray = {
    @javax.annotation.Nonnull val array = new JsonArray
    for (k <- kernelDims) {
      array.add(new JsonPrimitive(k))
    }
    array
  }

  /**
    * Write json.
    *
    * @param obj the obj
    * @return the char sequence
    */
  def toJson(obj: Any): CharSequence = toJson(obj, getMapper)

  @Nonnull def toJson(obj: Any, objectMapper: ObjectMapper): CharSequence = {
    val outputStream = new ByteArrayOutputStream
    try
      objectMapper.writeValue(outputStream, obj)
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }
    new String(outputStream.toByteArray, Charset.forName("UTF-8"))
  }

  @throws[IOException]
  def cache[T](file: File, clazz: Class[T], intializer: Supplier[T]): T = if (file.exists) getMapper.readValue(FileUtils.readFileToString(file, Charset.defaultCharset), clazz)
  else {
    val obj = intializer.get
    FileUtils.write(file, toJson(obj), Charset.defaultCharset)
    obj
  }

  /**
    * Gets mapper.
    *
    * @return the mapper
    */
  def getMapper: ObjectMapper = {
    val mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
}