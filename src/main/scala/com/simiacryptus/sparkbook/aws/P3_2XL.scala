/*
 * Copyright (c) 2020 by Andrew Charneski.
 *
 * The author licenses this file to you under the
 * Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.simiacryptus.sparkbook.aws

import com.simiacryptus.aws.exe.EC2NodeSettings
import com.simiacryptus.sparkbook.aws.Const._
import com.simiacryptus.sparkbook.{AWSNotebookRunner, EC2Runner}

trait P3_2XL extends EC2Runner[Object] with AWSNotebookRunner[Object] {
  override val s3bucket: String = "test.deepartist.org"

  override def nodeSettings: EC2NodeSettings = EC2NodeSettings.P3_2XL

  override def maxHeap: Option[String] = Option("50g")

  def className: String

  override def javaProperties: Map[String, String] = super.javaProperties ++ Map(
    "MAX_TOTAL_MEMORY" -> (14 * GiB).toString,
    "MAX_DEVICE_MEMORY" -> (14 * GiB).toString,
    "CUDA_DEFAULT_PRECISION" -> "Float",
    "MAX_FILTER_ELEMENTS" -> (512 * MiB).toString,
    "MAX_IO_ELEMENTS" -> (512 * MiB).toString
  )
}
