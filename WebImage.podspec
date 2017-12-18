require 'json'
version = JSON.parse(File.read('package.json'))['version']

Pod::Spec.new do |s|
  s.name            = 'WebImage'
  s.version         = version
  s.homepage        = 'https://github.com/vovkasm/react-native-web-image'
  s.summary         = 'An image component for react-native with persistent disk and memory caching'
  s.license         = 'MIT'
  s.author          = { 'Vladimir Timofeev' => 'vovkasm@gmail.com' }
  s.ios.deployment_target = '8.0'
  s.source          = { :git => 'https://github.com/vovkasm/react-native-web-image.git', :tag => "v#{s.version}" }
  s.source_files    = 'WebImage/WebImage/*.{h,m}'
  s.preserve_paths  = 'lib/*.js'
  s.frameworks      = 'Foundation', 'UIKit'
  s.dependency 'React'
  s.dependency 'SDWebImage'
end
