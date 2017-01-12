module.exports = {
  env: { browser: true },
  extends: [ 'standard', 'standard-react' ],
  plugins: [ 'import' ],
  parser: 'babel-eslint',
  rules: {
    // For readability (TODO: sort imports in same group)
    'import/order': ['error',{'newlines-between': 'always'}],
  }
}
