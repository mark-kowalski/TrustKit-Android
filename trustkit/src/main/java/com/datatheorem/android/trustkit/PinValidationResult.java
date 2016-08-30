package com.datatheorem.android.trustkit;


//todo put under the pinning package
public enum  PinValidationResult {
    PIN_VALIDATION_RESULT_SUCCESS,
    PIN_VALIDATION_RESULT_FAILED,
    PIN_VALIDATION_RESULT_FAILED_CERTIFICATE_CHAIN_NOT_TRUSTED,
    PIN_VALIDATION_RESULT_ERROR_INVALID_PARAMETERS,
    PIN_VALIDATION_RESULT_FAILED_USER_DEFINED_TRUST_ANCHOR,
    PIN_VALIDATION_RESULT_ERROR_COULD_NOT_GENERATE_SPKI_HASH
}
