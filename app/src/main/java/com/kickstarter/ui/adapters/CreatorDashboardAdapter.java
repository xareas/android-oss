package com.kickstarter.ui.adapters;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;

import com.kickstarter.R;
import com.kickstarter.models.Project;
import com.kickstarter.models.ProjectStats;
import com.kickstarter.ui.viewholders.CreatorDashboardHeaderViewHolder;
import com.kickstarter.ui.viewholders.KSViewHolder;

import java.util.Collections;

public class CreatorDashboardAdapter extends KSAdapter {

  protected @LayoutRes int layout(final @NonNull SectionRow sectionRow) {
    return R.layout.creator_dashboard_layout;
  }

  protected @NonNull KSViewHolder viewHolder(final @LayoutRes int layout, final @NonNull View view) {
    return new CreatorDashboardHeaderViewHolder(view);
  }

  /**
   * Populate adapter data.
   */
  public void takeProjectAndStats(final @NonNull Project project, final @NonNull ProjectStats projectStats) {
    sections().clear();
    sections().add(Collections.singletonList(Pair.create(project, projectStats)));
    notifyDataSetChanged();
  }
}
