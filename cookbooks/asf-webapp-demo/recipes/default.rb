# Encoding: utf-8
#
# Cookbook Name:: asf-webapp-demo
# Recipe:: default
#
# Copyright 2014, Copyright (c) 2014 Grid Dynamics International, Inc. All Rights Reserved
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

include_recipe 'yum-epel'

package 'tomcat' do
  action :install
end

execute 'fix tomcat.conf' do
  command 'sed \'s/JAVA_HOME=.*/JAVA_HOME=\/usr\/lib\/jvm\/java/\' /etc/tomcat/tomcat.conf'
  user 'root'
  action :run
end

service 'tomcat' do
  supports :status => true, :restart => true, :reload => true
  action [ :enable, :start ]
end

remote_file '/var/lib/tomcat/webapps/asf-webapp-demo.war' do
  owner 'tomcat'
  group 'tomcat'
  mode 0644
  source node['asf-webapp-demo']['url']
  checksum node['asf-webapp-demo']['checksum']
  notifies :restart, 'service[tomcat]'
end

ruby_block 'waiting for asf-webapp-demo' do
  block do
    require 'net/https'
    require 'uri'
    uri = URI('http://localhost:8080/asf-webapp-demo')
    10.times {
      sleep(30)
      break if Net::HTTP.get_response(uri) == '302'
    }
  end
end

