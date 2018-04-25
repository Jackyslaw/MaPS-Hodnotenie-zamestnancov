import replace from 'rollup-plugin-replace';
import alias from 'rollup-plugin-alias';
import vue from 'rollup-plugin-vue2';
import babel from 'rollup-plugin-babel';
import nodeResolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import scss from 'rollup-plugin-scss';
import uglify from 'rollup-plugin-uglify';

const plugins = [
    replace({
        'process.env.NODE_ENV': JSON.stringify('production') // or 'development'
    }),
 //  alias({
 //   'vue': process.env.NODE_ENV === 'production' ? './../node_modules/vue/dist/vue.common.js' : './../node_modules/vue/dist/vue.common.js',
 // }),
  vue(),
  scss({output: 'dist/app.css'}),
  babel({ exclude: 'node_modules/**' }),
  nodeResolve({ browser: true, jsnext: true }),
  commonjs(),
];

if (process.env.NODE_ENV === 'production') {
  plugins.push(uglify());
}

if (process.env.NODE_ENV === 'development') {
}

export default {
  input: 'src/main.js',
  sourcemap: false,
  treeshake: true,
  plugins,
  output: {
    file: 'dist/app.min.js',
    format: 'iife'
  }
};
