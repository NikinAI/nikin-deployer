# nikin-deployer

## Current issues
1. CRDs for SparkApps are not fully working I needed to remove `envSecretKeyRefs` to make that work, but that may not be a big issue as the `envSecretKeyRefs` is going to be removed in next release of operator.
