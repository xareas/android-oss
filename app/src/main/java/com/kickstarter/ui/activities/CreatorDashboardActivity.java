package com.kickstarter.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.kickstarter.KSApplication;
import com.kickstarter.R;
import com.kickstarter.libs.BaseActivity;
import com.kickstarter.libs.KSCurrency;
import com.kickstarter.libs.KSString;
import com.kickstarter.libs.RefTag;
import com.kickstarter.libs.qualifiers.RequiresActivityViewModel;
import com.kickstarter.libs.utils.ProjectUtils;
import com.kickstarter.models.Project;
import com.kickstarter.models.ProjectStats;
import com.kickstarter.ui.IntentKey;
import com.kickstarter.ui.adapters.CreatorDashboardAdapter;
import com.kickstarter.viewmodels.CreatorDashboardViewModel;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

import static com.kickstarter.libs.rx.transformers.Transformers.observeForUI;

@RequiresActivityViewModel(CreatorDashboardViewModel.ViewModel.class)
public final class CreatorDashboardActivity extends BaseActivity<CreatorDashboardViewModel.ViewModel> {

  protected @Bind(R.id.creator_dashboard_recycler_view) RecyclerView creatorDashboardRecyclerView;
  protected @Bind(R.id.creator_dashboard_amount_raised) TextView amountRaisedTextView;
  protected @Bind(R.id.creator_dashboard_funding_text) TextView fundingTextTextView;
  protected @Bind(R.id.creator_dashboard_backer_count) TextView backerCountTextView;
  protected @Bind(R.id.creator_dashboard_project_name) TextView projectNameTextView;
  protected @Bind(R.id.creator_dashboard_time_remaining) TextView timeRemainingTextView;
  protected @Bind(R.id.creator_dashboard_time_remaining_text) TextView timeRemainingTextTextView;
  protected @Bind(R.id.creator_dashboard_reward_count) TextView rewardCountTextView;
  protected @BindString(R.string.discovery_baseball_card_stats_pledged_of_goal) String pledgedOfGoalString;

  private KSCurrency ksCurrency;
  private KSString ksString;
  private CreatorDashboardAdapter adapter;

  @Override
  protected void onCreate(final @Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.creator_dashboard_layout);
    ButterKnife.bind(this);

    this.adapter = new CreatorDashboardAdapter();
    creatorDashboardRecyclerView.setAdapter(this.adapter);
    creatorDashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    this.ksCurrency = this.environment().ksCurrency();
    this.ksString = this.environment().ksString();

    viewModel.outputs.projectAndStats()
      .compose(bindToLifecycle())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(ps -> this.renderProjectAndStats(ps.first, ps.second));

    viewModel.outputs.latestProject()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(this::setTimeRemainingTextTextView);


    viewModel.outputs.startProjectActivity()
      .compose(bindToLifecycle())
      .compose(observeForUI())
      .subscribe(projectAndRefTag -> this.startProjectActivity(projectAndRefTag.first, projectAndRefTag.second));
  }

  @OnClick({ R.id.creator_view_project_button, R.id.creator_dashboard_project_context_view })
  protected void viewProjectButtonClicked() {
    this.viewModel.inputs.projectViewClicked();
  }

  private void startProjectActivity(final @NonNull Project project, final @NonNull RefTag refTag) {
    final Intent intent = new Intent(this, ProjectActivity.class)
      .putExtra(IntentKey.PROJECT, project)
      .putExtra(IntentKey.REF_TAG, refTag);
    startActivity(intent);
  }

  private void renderProjectAndStats(final @NonNull Project project, final @NonNull ProjectStats projectStats) {
    adapter.takeProjectAndStats(project, projectStats);
  }

  private void setPledgedOfGoalString(final @NonNull Project latestProject) {
    final String goalString = ksCurrency.format(latestProject.pledged(), latestProject, false, true, RoundingMode.DOWN);
    amountRaisedTextView.setText(goalString);

    final String goalText = ksString.format(this.pledgedOfGoalString, "goal", goalString);
    fundingTextTextView.setText(goalText);
  }

  private void setTimeRemainingTextTextView(final @NonNull Project latestProject) {
    timeRemainingTextTextView.setText(ProjectUtils.deadlineCountdownDetail(latestProject, this, ksString));
  }
}
