def appDirectory = ""

pipeline {
    agent any
    stages {
        stage("checkout SCM") {   
            steps {
                script {
                    sh "rm -rf *"
                    git env.GIT_REPOSITORY_APP
                    appDirectory = env.GIT_REPOSITORY_APP.substring(env.GIT_REPOSITORY_APP.lastIndexOf("/") + 1, (env.GIT_REPOSITORY_APP.length() - 1))
                    echo "App is in dir ${appDirectory}"
                    // just debug info
                    sh "pwd && ls -la"
                    sh "mkdir ${appDirectory} && rsync -av --progress * ${appDirectory} --exclude ${appDirectory}"
                    stash name: 'app_repo', includes: "${appDirectory}/**"
                }
            }
        }
        stage("build without cache") {
            agent {
                label "nodejs"
            }
            steps {
                script {
                    unstash name: "app_repo"
                    sh "cd ${appDirectory} && npm install"
                }
            }
        }
        stage("build with cache") {
            agent {
                // this image has a pre-build npm install command on the deps of this app
                label "nodejs-cache"
            }
            steps {
                script {
                    unstash name: "app_repo"
                    sh "cd ${appDirectory} && npm install"
                }
            }
        }
    }
}