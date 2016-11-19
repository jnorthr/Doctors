tmpDir = File.createTempDir('fred') 
def fileTreeBuilder = new FileTreeBuilder(tmpDir) 

fileTreeBuilder.dir('jnorthr') {
   file('travis.yml', 'this is a yaml file')
   dir('src') {
      dir('main') {
          dir('groovy') {
            file('build.gradle', 'build a gradle project')
          } // end of groovy

      } // end of main
   } 
}

println "--- the end ---"