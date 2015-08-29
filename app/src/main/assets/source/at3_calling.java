
// Calling Activity
explode = new Explode();
propagation = new CircularPropagation();
propagation.setPropagationSpeed(1f);
explode.setPropagation(propagation);
getWindow().setReenterTransition(explode);
getWindow().setExitTransition(explode);
