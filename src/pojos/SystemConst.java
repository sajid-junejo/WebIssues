/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 * Definitions of various global constants.
 */
public class SystemConst {

    /**
     * Various Constants
     */
    public static final int INT_MAX = 2147483647;

    /**
     * Access Levels
     */
    public static final int NoAccess = 0;
    public static final int NormalAccess = 1;
    public static final int AdministratorAccess = 2;

    /**
     * Alert Types
     */
    public static final int Alert = 1;
    public static final int ChangeReport = 2;
    public static final int IssueReport = 3;

    /**
     * Report Frequencies
     */
    public static final int Daily = 0;
    public static final int Weekly = 1;

    /**
     * Issue Changes
     */
    public static final int IssueCreated = 0;
    public static final int IssueRenamed = 1;
    public static final int ValueChanged = 2;
    public static final int CommentAdded = 3;
    public static final int FileAdded = 4;
    public static final int IssueMoved = 5;

    /**
     * Text Formats
     */
    public static final int PlainText = 0;
    public static final int TextWithMarkup = 1;

    /**
     * Sort Order
     */
    public static final int Ascending = 0;
    public static final int Descending = 1;

    /**
     * Email Formats
     */
    public static final int SeparateAttachmentsFormat = 0;
    public static final int EmlFormat = 1;

    /**
     * Limits
     */
    public static final int NameMaxLength = 40;
    public static final int ValueMaxLength = 255;
    public static final int FileNameMaxLength = 80;
    public static final int DescriptionMaxLength = 255;
    public static final int KeyMaxLength = 40;
    public static final int LoginMaxLength = 40;
    public static final int PasswordMaxLength = 40;
}
