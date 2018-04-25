import Lockr from './lockr';
import Vue from 'vue';

Lockr.prefix = 'fibimeter_';

import { object } from './utils'

export default {
  get(url, data, callback, showLoading = true, headers = []) {
    callAjax(url, data, 'GET', callback, showLoading, headers);
  },
  post(url, data, callback, showLoading = true, headers = []) {
    callAjax(url, data, 'POST', callback, showLoading, headers);
  }
}

function callAjax(url, data, method, callback, showLoading = true, headers = []) {
  $.ajax({
    url,
    contentType: "application/json;charset=utf-8",
    beforeSend: function(request) {
      if(showLoading) {
        Vue.prototype.$eventBus.$emit('loadingStart');
      }
      const jwt = Lockr.get('jwt', false);
      if(jwt) {
          request.setRequestHeader('Authorization', jwt);
      }
    },
    method,
    data: method.toLowerCase() === 'get' ? data : JSON.stringify(data),
    success: (data, status, xhr) => callback({
        data: normalize(data),
        headers: object.flatten(headers.map(h => ({ [h]: xhr.getResponseHeader(h) }))),
    }),
    statusCode: {
      401: () => callback(null, { code: 401 }),
      500: () => callback(null, { code: 500 }),
    },
    complete: () => {
      if(showLoading) {
        Vue.prototype.$eventBus.$emit('loadingEnd');
      }
    }
  });
}


function normalize(data) {
  if(typeof data === 'string') {
    return JSON.parse(data);
  }
  return data;
}
