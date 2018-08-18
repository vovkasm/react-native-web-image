require 'json'
package = JSON.parse(File.read('package.json'))

Pod::Spec.new do |s|
  s.name            = 'react-native-web-image'
  s.version         = package['version']
  s.homepage        = package['homepage']
  s.summary         = 'An image component for react-native with persistent disk and memory caching'
  s.license         = 'MIT'
  s.author          = { 'Vladimir Timofeev' => 'vovkasm@gmail.com' }
  s.ios.deployment_target = '8.0'
  s.source          = { :git => 'https://github.com/vovkasm/react-native-web-image.git', :tag => "v#{s.version}" }
  s.source_files    = 'WebImage/WebImage/*.{h,m}'
  s.preserve_paths  = 'dist/*'
  s.frameworks      = 'Foundation', 'UIKit'

  s.dependency 'React'
  s.dependency 'SDWebImage'
end
