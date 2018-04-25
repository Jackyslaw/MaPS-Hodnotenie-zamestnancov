const ROOT = '/';

import Vue from 'vue'
import App from './components/App.vue';
import VueRouter from 'vue-router/dist/vue-router.min.js';

Vue.use(VueRouter);

import IndexScreen from './components/IndexScreen.vue';
import FibimeterScreen from './components/FibimeterScreen.vue';

const router = new VueRouter({
    routes: [
        { path: ROOT, component: IndexScreen },
        { path: ROOT + 'fibimeter', component: FibimeterScreen },
    ],
    mode: 'history',
});

Vue.prototype.$eventBus = new Vue();

import { stringUtil } from './utils';

Vue.prototype.FIBIMETER_CONSTANTS = {
    orderManagementLevels: ['lower', 'middle', 'higher'],
    lvlStringRepresentation(lvl) {
        return stringUtil.capitalizeFirstLetter(lvl) + ' Management';
    },
    getManagementLevels: () => {
        return Vue.prototype.FIBIMETER_CONSTANTS.orderManagementLevels.map((lvl) => {
            const title = Vue.prototype.FIBIMETER_CONSTANTS.lvlStringRepresentation(lvl);
            return { title, value: lvl };
        });
    },
    getFullNameFromEmployee({ academicTitle, firstName, surname }) {
        const ac = academicTitle;
        const containsPhD = ac ? ac.toLowerCase().startsWith('phd') : null;
        return (ac && !containsPhD ? ac + ' ' : '')
            + firstName + ' ' + surname
            + (containsPhD ? ' ' + ac : '');
    }
}

Vue.prototype.$commonGlobals = {
}


const app = new Vue({
    el: '#app',
    router,
    render: h => h(App),
});