package com.sgh.swinburne.heartplus.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.sgh.swinburne.heartplus.R;

/**
 * Created by faiza on 5/16/2016.
 */
public class Disclaimer {

    private String EULA_PREFIX = "eula_";
    private Activity mActivity;

    public Disclaimer(Activity context) {
        mActivity = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // the eulaKey changes every time you increment the version number in the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
        if (hasBeenShown == false) {

            // Show the Eula
            String title = mActivity.getString(R.string.app_name);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage("By downloading any application from Sarawak Genral or Google™ (here after referered to as “The Company”), installing or using this application or any portion thereof (“Application”), you agree to the following terms and conditions (the “Terms and Conditions”).\n" +
                            "\n" +
                            "1. USE OF APPLICATION\n" +
                            "a. The Company grants you the non-exclusive, non-transferable, limited right and license to install and use this Application solely and exclusively for your personal use.\n" +
                            "b. You may not use the Application in any manner that could damage, disable, overburden, or impair the Application (or servers or networks connected to the Application), nor may you use the Application in any manner that could interfere with any other party’s use and enjoyment of the Application (or servers or networks connected to the Application).\n" +
                            "c. You agree that you are solely responsible for (and that The Company has no responsibility to you or to any third party for) your use of the Application, any breach of your obligations under the Terms and Conditions, and for the consequences (including any loss or damage which The Company may suffer) of any such breach.\n" +
                            "\n" +
                            "2. PROPRIETARY RIGHTS\n" +
                            "You acknowledge that (a) the Application contains proprietary and confidential information that is protected by applicable intellectual property and other laws, and (b) The Company and/or third parties own all right, title and interest in and to the Application and content, excluding content provided by you, that may be presented or accessed through the Application, including without limitation all Intellectual Property Rights therein and thereto. “Intellectual Property Rights” means any and all rights existing from time to time under patent law, copyright law, trade secret law, trademark law, unfair competition law, and any and all other proprietary rights, and any and all applications, renewals, extensions and restorations thereof, now or hereafter in force and effect worldwide. You agree that you will not, and will not allow any third party to, (i) copy, sell, license, distribute, transfer, modify, adapt, translate, prepare derivative works from, decompile, reverse engineer, disassemble or otherwise attempt to derive source code from the Application or content that may be presented or accessed through the Application for any purpose, unless otherwise permitted, (ii) take any action to circumvent or defeat the security or content usage rules provided, deployed or enforced by any functionality (including without limitation digital rights management functionality) contained in the Application, (iii) use the Application to access, copy, transfer, transcode or retransmit content in violation of any law or third party rights, or (iv) remove, obscure, or alter The Company’s or any third partyâ€™s copyright notices, trademarks, or other proprietary rights notices affixed to or contained within or accessed in conjunction with or through the Application.\n" +
                            "\n" +
                            "3. THE COMPANY TERMS OF SERVICE AND PRIVACY POLICY\n" +
                            "a. The Company’s Privacy Policy (located at http://www.google.com/privacypolicy.html) explains how The Company treats your information and protects your privacy when you use the Application. You agree to the use of your data in accordance with The Company’s privacy policies.\n" +
                            "b. The Application may contain features that are used in conjunction with The Company’s search and other services. Accordingly, your use of such features of the Application is also governed by The Company’s Terms of Service located at http://www.google.com/terms_of_service.html, The Company’s Privacy Policy located at http://www.google.com/privacypolicy.html, as well as any applicable The Company Service-specific Terms of Service and Privacy Policy, which may be updated from time to time and without notice.\n" +
                            "\n" +
                            "4. U.S. GOVERNMENT RESTRICTED RIGHTS\n" +
                            "This Application, related materials and documentation have been developed entirely with private funds. If the user of the Application is an agency, department, employee, or other entity of the United States Government, the use, duplication, reproduction, release, modification, disclosure, or transfer of the Application, including technical data or manuals, is restricted by the terms, conditions and covenants contained in these Terms and Conditions. In accordance with Federal Acquisition Regulation 12.212 for civilian agencies and Defense Federal Acquisition Regulation Supplement 227.7202 for military agencies, use of the Application is further restricted by these Terms and Conditions.\n" +
                            "\n" +
                            "5. EXPORT RESTRICTIONS\n" +
                            "The Application may be subject to export controls or restrictions by the United States or other countries or territories. You agree to comply with all applicable U.S. and international export laws and regulations. These laws include restrictions on destinations, end users, and end use.\n" +
                            "\n" +
                            "6. TERMINATION\n" +
                            "These Terms and Conditions will continue to apply until terminated by either you or The Company as set forth below. You may terminate these Terms and Conditions at any time by permanently deleting the Application from your mobile device in its entirety. Your rights automatically and immediately terminate without notice from The Company or any Third Party if you fail to comply with any provision of these Terms and Conditions. In such event, you must immediately delete the Application.\n" +
                            "\n" +
                            "7. INDEMNITY\n" +
                            "To the maximum extent permitted by law, you agree to defend, indemnify and hold harmless The Company, its affiliates and their respective directors, officers, employees and agents from and against any and all claims, actions, suits or proceedings, as well as any and all losses, liabilities, damages, costs and expenses (including reasonable attorneys fees) arising out of or accruing from your use of the Application, including your downloading, installation, or use of the Application, or your violation of these Terms and Conditions.\n" +
                            "\n" +
                            "8. DISCLAIMER OF WARRANTIES\n" +
                            "a. YOU EXPRESSLY UNDERSTAND AND AGREE THAT YOUR USE OF THE APPLICATION IS AT YOUR SOLE DISCRETION AND RISK AND THAT THE APPLICATION IS PROVIDED AS IS AND AS AVAILABLE WITHOUT WARRANTY OF ANY KIND.\n" +
                            "b. YOU ARE SOLELY RESPONSIBLE FOR ANY DAMAGE TO YOUR MOBILE DEVICE, OR OTHER DEVICE, OR LOSS OF DATA THAT RESULTS FROM SUCH USE.\n" +
                            "c. THE COMPANY FURTHER EXPRESSLY DISCLAIMS ALL WARRANTIES AND CONDITIONS OF ANY KIND, WHETHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT, WITH RESPECT TO THE APPLICATION.\n" +
                            "d. THE APPLICATION IS NOT INTENDED FOR USE IN THE OPERATION OF NUCLEAR FACILITIES, LIFE SUPPORT SYSTEMS, EMERGENCY COMMUNICATIONS, AIRCRAFT NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL SYSTEMS, OR ANY OTHER ACTIVITIES IN WHICH THE FAILURE OF THE APPLICATION COULD LEAD TO DEATH, PERSONAL INJURY, OR SEVERE PHYSICAL OR ENVIRONMENTAL DAMAGE.\n" +
                            "\n" +
                            "9. LIMITATION OF LIABILITY\n" +
                            "YOU EXPRESSLY UNDERSTAND AND AGREE THAT THE COMPANY, ITS SUBSIDIARIES AND AFFILIATES, AND ITS LICENSORS ARE NOT LIABLE TO YOU UNDER ANY THEORY OF LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL CONSEQUENTIAL OR EXEMPLARY DAMAGES THAT MAY BE INCURRED BY YOU THROUGH YOUR USE OF THE APPLICATION, INCLUDING ANY LOSS OF DATA OR DAMAGE TO YOUR MOBILE DEVICE, WHETHER OR NOT THE COMPANY OR ITS REPRESENTATIVES HAVE BEEN ADVISED OF OR SHOULD HAVE BEEN AWARE OF THE POSSIBILITY OF ANY SUCH LOSSES ARISING.\n" +
                            "\n" +
                            " \n" +
                            "\n" +
                            "10. MISCELLANEOUS\n" +
                            "a. These Terms and Conditions constitute the entire Agreement between you and The Company relating to the Application and govern your use of the Application, and completely replace any prior or contemporaneous agreements between you and The Company regarding the Application.\n" +
                            "b. The failure of The Company to exercise or enforce any right or provision of these Terms and Conditions does not constitute a waiver of such right or provision, which will still be available to The Company.\n" +
                            "c. If any court of law, having the jurisdiction to decide on this matter, rules that any provision of these Terms and Conditions is invalid, then that provision will be removed from the Terms and Conditions without affecting the rest of the Terms and Conditions. The remaining provisions of these Terms and Conditions will continue to be valid and enforceable.\n" +
                            "d. The rights granted in these Terms and Conditions may not be assigned or transferred by either you or The Company without the prior written approval of the other party. Neither you nor The Company are permitted to delegate their responsibilities or obligations under these Terms and Conditions without the prior written approval of the other party.\n" +
                            "e. These Terms and Conditions and your relationship with The Company under these Terms and Conditions will be governed by the laws of the State of California without regard to its conflict of laws provisions. You and The Company agree to submit to the exclusive jurisdiction of the courts located within the county of Santa Clara, California to resolve any legal matter arising from these Terms and Conditions. Notwithstanding this, you agree that The Company will still be allowed to apply for injunctive remedies (or an equivalent type of urgent legal relief) in any jurisdiction.")

                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the EULA
                            mActivity.finish();
                        }

                    });
            builder.create().show();
        }

    }
}
