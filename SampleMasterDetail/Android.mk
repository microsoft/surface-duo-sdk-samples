#
# Copyright (C) 2013 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)

#
# Build rule for app
#
include $(CLEAR_VARS)
LOCAL_USE_AAPT2 := true
LOCAL_MODULE_TAGS := optional
#LOCAL_MODULE_PATH := $(TARGET_OUT_VENDOR_APPS)

LOCAL_SRC_FILES := \
    $(call all-java-files-under, SampleMasterDetail/src/main/java)

#LOCAL_PROGUARD_FLAG_FILES := proguard.flags

#LOCAL_SDK_VERSION := current
#LOCAL_MIN_SDK_VERSION := 27
LOCAL_PACKAGE_NAME := SampleMasterDetail
LOCAL_PRIVILEGED_MODULE := true
LOCAL_PRIVATE_PLATFORM_APIS := true

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/SampleMasterDetail/src/main/res
LOCAL_MANIFEST_FILE := SampleMasterDetail/src/main/AndroidManifest.xml

LOCAL_JACK_COVERAGE_INCLUDE_FILTER := com.oema0.sampleapp.masterdetail*

LOCAL_JAVA_LIBRARIES := com.oema0.eos.sdk
LOCAL_STATIC_ANDROID_LIBRARIES := \
    androidx.appcompat_appcompat

include $(BUILD_PACKAGE)

