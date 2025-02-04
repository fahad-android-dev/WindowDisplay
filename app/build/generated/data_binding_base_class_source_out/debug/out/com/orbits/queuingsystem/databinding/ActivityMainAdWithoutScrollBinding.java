// Generated by view binder compiler. Do not edit!
package com.orbits.queuingsystem.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.orbits.queuingsystem.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainAdWithoutScrollBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout llContainerLogoAdToken;

  @NonNull
  public final LinearLayout llContainerLogoAds;

  @NonNull
  public final LinearLayout llFragmentTokenContainer;

  @NonNull
  public final LinearLayout llMainActivityTopParent;

  private ActivityMainAdWithoutScrollBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout llContainerLogoAdToken, @NonNull LinearLayout llContainerLogoAds,
      @NonNull LinearLayout llFragmentTokenContainer,
      @NonNull LinearLayout llMainActivityTopParent) {
    this.rootView = rootView;
    this.llContainerLogoAdToken = llContainerLogoAdToken;
    this.llContainerLogoAds = llContainerLogoAds;
    this.llFragmentTokenContainer = llFragmentTokenContainer;
    this.llMainActivityTopParent = llMainActivityTopParent;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainAdWithoutScrollBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainAdWithoutScrollBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main_ad_without_scroll, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainAdWithoutScrollBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ll_container_logo_ad_token;
      LinearLayout llContainerLogoAdToken = ViewBindings.findChildViewById(rootView, id);
      if (llContainerLogoAdToken == null) {
        break missingId;
      }

      id = R.id.ll_container_logo_ads;
      LinearLayout llContainerLogoAds = ViewBindings.findChildViewById(rootView, id);
      if (llContainerLogoAds == null) {
        break missingId;
      }

      id = R.id.ll_fragment_token_container;
      LinearLayout llFragmentTokenContainer = ViewBindings.findChildViewById(rootView, id);
      if (llFragmentTokenContainer == null) {
        break missingId;
      }

      LinearLayout llMainActivityTopParent = (LinearLayout) rootView;

      return new ActivityMainAdWithoutScrollBinding((LinearLayout) rootView, llContainerLogoAdToken,
          llContainerLogoAds, llFragmentTokenContainer, llMainActivityTopParent);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
