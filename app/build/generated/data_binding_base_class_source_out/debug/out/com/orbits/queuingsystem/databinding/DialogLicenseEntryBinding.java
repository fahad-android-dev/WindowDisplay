// Generated by view binder compiler. Do not edit!
package com.orbits.queuingsystem.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.orbits.queuingsystem.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogLicenseEntryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnExitLicense;

  @NonNull
  public final Button btnGetFullLicense;

  @NonNull
  public final Button btnGetTrialLicense;

  @NonNull
  public final Button btnSaveLicense;

  @NonNull
  public final EditText etBranchCode;

  @NonNull
  public final EditText etCustCode;

  @NonNull
  public final EditText etEmail;

  @NonNull
  public final EditText etInvNum;

  @NonNull
  public final EditText etPhone;

  @NonNull
  public final EditText etStaffId;

  @NonNull
  public final EditText etStaffPwd;

  @NonNull
  public final TextView tvBranchCode;

  @NonNull
  public final TextView tvCustCode;

  @NonNull
  public final TextView tvEmail;

  @NonNull
  public final TextView tvInfo;

  @NonNull
  public final TextView tvInstructions;

  @NonNull
  public final TextView tvInvNum;

  @NonNull
  public final TextView tvPhone;

  @NonNull
  public final TextView tvStaffId;

  @NonNull
  public final TextView tvStaffPwd;

  @NonNull
  public final TextView txtLicenseDialogTitle;

  private DialogLicenseEntryBinding(@NonNull LinearLayout rootView, @NonNull Button btnExitLicense,
      @NonNull Button btnGetFullLicense, @NonNull Button btnGetTrialLicense,
      @NonNull Button btnSaveLicense, @NonNull EditText etBranchCode, @NonNull EditText etCustCode,
      @NonNull EditText etEmail, @NonNull EditText etInvNum, @NonNull EditText etPhone,
      @NonNull EditText etStaffId, @NonNull EditText etStaffPwd, @NonNull TextView tvBranchCode,
      @NonNull TextView tvCustCode, @NonNull TextView tvEmail, @NonNull TextView tvInfo,
      @NonNull TextView tvInstructions, @NonNull TextView tvInvNum, @NonNull TextView tvPhone,
      @NonNull TextView tvStaffId, @NonNull TextView tvStaffPwd,
      @NonNull TextView txtLicenseDialogTitle) {
    this.rootView = rootView;
    this.btnExitLicense = btnExitLicense;
    this.btnGetFullLicense = btnGetFullLicense;
    this.btnGetTrialLicense = btnGetTrialLicense;
    this.btnSaveLicense = btnSaveLicense;
    this.etBranchCode = etBranchCode;
    this.etCustCode = etCustCode;
    this.etEmail = etEmail;
    this.etInvNum = etInvNum;
    this.etPhone = etPhone;
    this.etStaffId = etStaffId;
    this.etStaffPwd = etStaffPwd;
    this.tvBranchCode = tvBranchCode;
    this.tvCustCode = tvCustCode;
    this.tvEmail = tvEmail;
    this.tvInfo = tvInfo;
    this.tvInstructions = tvInstructions;
    this.tvInvNum = tvInvNum;
    this.tvPhone = tvPhone;
    this.tvStaffId = tvStaffId;
    this.tvStaffPwd = tvStaffPwd;
    this.txtLicenseDialogTitle = txtLicenseDialogTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogLicenseEntryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogLicenseEntryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_license_entry, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogLicenseEntryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_exit_license;
      Button btnExitLicense = ViewBindings.findChildViewById(rootView, id);
      if (btnExitLicense == null) {
        break missingId;
      }

      id = R.id.btn_get_full_license;
      Button btnGetFullLicense = ViewBindings.findChildViewById(rootView, id);
      if (btnGetFullLicense == null) {
        break missingId;
      }

      id = R.id.btn_get_trial_license;
      Button btnGetTrialLicense = ViewBindings.findChildViewById(rootView, id);
      if (btnGetTrialLicense == null) {
        break missingId;
      }

      id = R.id.btn_save_license;
      Button btnSaveLicense = ViewBindings.findChildViewById(rootView, id);
      if (btnSaveLicense == null) {
        break missingId;
      }

      id = R.id.et_branch_code;
      EditText etBranchCode = ViewBindings.findChildViewById(rootView, id);
      if (etBranchCode == null) {
        break missingId;
      }

      id = R.id.et_cust_code;
      EditText etCustCode = ViewBindings.findChildViewById(rootView, id);
      if (etCustCode == null) {
        break missingId;
      }

      id = R.id.et_email;
      EditText etEmail = ViewBindings.findChildViewById(rootView, id);
      if (etEmail == null) {
        break missingId;
      }

      id = R.id.et_inv_num;
      EditText etInvNum = ViewBindings.findChildViewById(rootView, id);
      if (etInvNum == null) {
        break missingId;
      }

      id = R.id.et_phone;
      EditText etPhone = ViewBindings.findChildViewById(rootView, id);
      if (etPhone == null) {
        break missingId;
      }

      id = R.id.et_staff_id;
      EditText etStaffId = ViewBindings.findChildViewById(rootView, id);
      if (etStaffId == null) {
        break missingId;
      }

      id = R.id.et_staff_pwd;
      EditText etStaffPwd = ViewBindings.findChildViewById(rootView, id);
      if (etStaffPwd == null) {
        break missingId;
      }

      id = R.id.tv_branch_code;
      TextView tvBranchCode = ViewBindings.findChildViewById(rootView, id);
      if (tvBranchCode == null) {
        break missingId;
      }

      id = R.id.tv_cust_code;
      TextView tvCustCode = ViewBindings.findChildViewById(rootView, id);
      if (tvCustCode == null) {
        break missingId;
      }

      id = R.id.tv_email;
      TextView tvEmail = ViewBindings.findChildViewById(rootView, id);
      if (tvEmail == null) {
        break missingId;
      }

      id = R.id.tvInfo;
      TextView tvInfo = ViewBindings.findChildViewById(rootView, id);
      if (tvInfo == null) {
        break missingId;
      }

      id = R.id.tvInstructions;
      TextView tvInstructions = ViewBindings.findChildViewById(rootView, id);
      if (tvInstructions == null) {
        break missingId;
      }

      id = R.id.tv_inv_num;
      TextView tvInvNum = ViewBindings.findChildViewById(rootView, id);
      if (tvInvNum == null) {
        break missingId;
      }

      id = R.id.tv_phone;
      TextView tvPhone = ViewBindings.findChildViewById(rootView, id);
      if (tvPhone == null) {
        break missingId;
      }

      id = R.id.tv_staff_id;
      TextView tvStaffId = ViewBindings.findChildViewById(rootView, id);
      if (tvStaffId == null) {
        break missingId;
      }

      id = R.id.tv_staff_pwd;
      TextView tvStaffPwd = ViewBindings.findChildViewById(rootView, id);
      if (tvStaffPwd == null) {
        break missingId;
      }

      id = R.id.txtLicenseDialogTitle;
      TextView txtLicenseDialogTitle = ViewBindings.findChildViewById(rootView, id);
      if (txtLicenseDialogTitle == null) {
        break missingId;
      }

      return new DialogLicenseEntryBinding((LinearLayout) rootView, btnExitLicense,
          btnGetFullLicense, btnGetTrialLicense, btnSaveLicense, etBranchCode, etCustCode, etEmail,
          etInvNum, etPhone, etStaffId, etStaffPwd, tvBranchCode, tvCustCode, tvEmail, tvInfo,
          tvInstructions, tvInvNum, tvPhone, tvStaffId, tvStaffPwd, txtLicenseDialogTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
