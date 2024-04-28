# Steps to creaet jenkins pipeline

1. Set credentials for Github/Docker/Nexus etc. **GitHub Tokens** shloud have premissions to hooks and commit status
2. If publish servers are needed - install publish over SSH plugin
3. Configure SSH Servers in Jenkins configuration (from Publish Over SSH plugin) to prod/test etc. **Name** is important for pipeline script!
4. Set credentials for these servers - ssh-keygen, then copy private key to jenkins/username + password. **ID** is importat for pipeline script!
5. Configure MultiBranch Pipeline to watch over code commits with GitHub creds on certain branches.
6. Configure Jenkinsfile pipeline
