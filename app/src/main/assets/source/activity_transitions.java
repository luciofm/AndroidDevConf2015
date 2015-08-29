
  view.setTransitionName("shared_hero");

  Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);

  ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, hero, "shared_hero");

  startActivity(intent, options.toBundle());
