require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "react-native-web-image"
  s.version      = package['version']
  s.summary      = "An image component for react-native with persistent disk and memory caching."

  s.authors      = { "vovkasm" => "vovkasm@gmail.com" }
  s.homepage     = "https://github.com/classpass/react-native-web-image"
  s.license      = package['license']
  s.platform     = :ios, "9.0"

  s.source       = { :git => "https://github.com/classpass/react-native-web-image.git", :tag => "v#{s.version}" }
  s.source_files  = "WebImage/WebImage/*.{h,m}"

  s.dependency 'React'
  s.dependency 'SDWebImage'
end
