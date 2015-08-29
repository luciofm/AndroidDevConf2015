  tm = TransitionInflater.from(getActivity())
          .inflateTransitionManager(R.transition.scene_transition, root);

  scene1 = Scene.getSceneForLayout(root, R.layout.scene_scene1, getActivity());
  scene2 = Scene.getSceneForLayout(root, R.layout.scene_scene2, getActivity());

  // go to scene2
  tm.transitionTo(scene2);

  // go to scene1
  tm.transitionTo(scene1);