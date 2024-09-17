pipeline{

agent any

tools{
  maven 'maven3.9.0'
}

stages{

stage('Checkoutcode') {
steps{
git branch: 'development', credentialsId: 'git-creds', url: 'https://github.com/deexithshetty/maven-web-application.git'
}
}

stage('Compile code') {
steps{
sh "mvn compile"
}
}

stage('Test code') {
steps{
sh "mvn test"
}
}

stage('Scan the file system') {
steps{
sh "trivy fs --format table -o trivy_report.html ."
}
}

stage('SonarQube report') {
steps{
sh "mvn sonar:sonar"
}
}

stage('Build') {
steps{
sh "mvn package"
}
}

/*
stage('Push to Nexus') {
steps{
sh "mvn deploy"
}
}
*/

stage('Build dockerimage') {
steps{
script{
withDockerRegistry(credentialsId: 'docker-creds') {
sh "docker build -t dockerdeexith/myapp-dev:latest ."
}
}
}
}

stage('Scan docker image') {
steps{
sh "trivy image --format table -o trivy-image-report.html dockerdeexith/myapp-dev:latest"
}
}

stage('Push docker image') {
steps{
script{
withDockerRegistry(credentialsId: 'docker-creds') {
sh "docker push dockerdeexith/myapp-dev:latest"
}
}
}
}

stage('deploy to k8s cluster') {
steps{
script{
withKubeConfig(caCertificate: '', clusterName: 'kubernetes', contextName: '', credentialsId: 'k8s-creds', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://172.31.37.250:6443') {
    sh "kubectl apply -f tomcat-configmap.yaml"
    sh "kubectl apply -f deployment.yaml" 
}

}
}
}

} // stages closing
post {
    always {
        script {
            def jobName = env.JOB_NAME
            def buildNumber = env.BUILD_NUMBER
            def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
            def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'

            def body = """
                <html>
                <body>
                <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                <h2>${jobName} - Build ${buildNumber}</h2>
                <div style="background-color: ${bannerColor}; padding: 10px;">
                <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
                </div>
                <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
                </div>
                </body>
                </html>
            """

            emailext (
                subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                body: body,
                to: 'deexith555@gmail.com',
                from: 'jenkins@example.com',
                replyTo: 'jenkins@example.com',
                mimeType: 'text/html',
                attachmentsPattern: 'trivy_report.html'
            )
        }
    }
}
} // pipeline closing
