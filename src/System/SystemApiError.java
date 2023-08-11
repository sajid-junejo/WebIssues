/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

public class SystemApiError extends SystemCoreException {
    public static final String LoginRequired = "300 Login Required";
    public static final String AccessDenied = "301 Access Denied";
    public static final String IncorrectLogin = "302 Incorrect Login";
    public static final String UnknownProject = "303 Unknown Project";
    public static final String UnknownFolder = "304 Unknown Folder";
    public static final String UnknownIssue = "305 Unknown Issue";
    public static final String UnknownFile = "306 Unknown File";
    public static final String UnknownUser = "307 Unknown User";
    public static final String UnknownType = "308 Unknown Type";
    public static final String UnknownAttribute = "309 Unknown Attribute";
    public static final String UnknownEvent = "310 Unknown Event";
    public static final String ProjectAlreadyExists = "311 Project Already Exists";
    public static final String FolderAlreadyExists = "312 Folder Already Exists";
    public static final String UserAlreadyExists = "313 User Already Exists";
    public static final String TypeAlreadyExists = "314 Type Already Exists";
    public static final String AttributeAlreadyExists = "315 Attribute Already Exists";
    public static final String CannotDeleteProject = "316 Cannot Delete Project";
    public static final String CannotDeleteFolder = "317 Cannot Delete Folder";
    public static final String CannotDeleteType = "318 Cannot Delete Type";
    public static final String InvalidString = "319 Invalid String";
    public static final String InvalidAccessLevel = "320 Invalid Access Level";
    public static final String InvalidValue = "321 Invalid Value";
    public static final String InvalidDefinition = "322 Invalid Definition";
    public static final String InvalidPreference = "323 Invalid Preference";
    public static final String InvalidSetting = "324 Invalid Setting";
    public static final String EmptyValue = "325 Empty Value";
    public static final String StringTooShort = "326 String Too Short";
    public static final String StringTooLong = "327 String Too Long";
    public static final String NumberTooLittle = "328 Number Too Little";
    public static final String NumberTooGreat = "329 Number Too Great";
    public static final String TooManyDecimals = "330 Too Many Decimals";
    public static final String TooManyDigits = "331 Too Many Digits";
    public static final String InvalidFormat = "332 Invalid Format";
    public static final String InvalidDate = "333 Invalid Date";
    public static final String InvalidTime = "334 Invalid Time";
    public static final String InvalidEmail = "335 Invalid Email";
    public static final String NoMatchingItem = "336 No Matching Item";
    public static final String DuplicateItems = "337 Duplicate Items";
    public static final String InvalidLimits = "338 Invalid Limits";
    public static final String IncompatibleType = "339 Incompatible Type";
    public static final String UnknownView = "340 Unknown View";
    public static final String UnknownColumn = "341 Unknown Column";
    public static final String ViewAlreadyExists = "342 View Already Exists";
    public static final String MissingColumn = "343 Missing Column";
    public static final String MissingAttribute = "344 Missing Attribute";
    public static final String NoItems = "345 No Items";
    public static final String PasswordNotMatching = "346 Password Not Matching";
    public static final String UnknownAlert = "347 Unknown Alert";
    public static final String AlertAlreadyExists = "348 Alert Already Exists";
    public static final String InvalidAlertEmail = "349 Invalid Alert Email";
    public static final String UnknownComment = "350 Unknown Comment";
    public static final String CannotDeleteAttribute = "351 Cannot Delete Attribute";
    public static final String MustChangePassword = "352 Must Change Password";
    public static final String CannotReusePassword = "353 Cannot Reuse Password";
    public static final String ItemNotFound = "354 Item Not Found";
    public static final String CommaNotAllowed = "355 Comma Not Allowed";
    public static final String TransactionDeadlock = "356 Transaction Deadlock";
    public static final String ConstraintConflict = "357 Constraint Conflict";
    public static final String EmailAlreadyExists = "358 Email Already Exists";
    public static final String InvalidActivationKey = "359 Invalid Activation Key";
    public static final String UnknownRequest = "360 Unknown Request";
    public static final String UnknownDescription = "361 Unknown Description";
    public static final String DescriptionAlreadyExists = "362 Description Already Exists";
    public static final String InvalidTextFormat = "363 Invalid Text Format";
    public static final String UnknownSubscription = "364 Unknown Subscription";
    public static final String SubscriptionAlreadyExists = "365 Subscription Already Exists";
    public static final String LoginAlreadyExists = "366 Login Already Exists";
    public static final String InvalidResetKey = "367 Invalid Reset Key";
    public static final String UnknownInbox = "368 Unknown Inbox";
    public static final String InvalidAlertType = "369 Invalid Alert Type";
    public static final String InvalidAlertFrequency = "370 Invalid Alert Frequency";
    public static final String InvalidCsrfToken = "371 Invalid Csrf Token";

    public SystemApiError(String message, Exception wrappedException) {
        super(message, wrappedException);
    }
}
