<template>
    <div class="managers-search">
        <div>
            <slot></slot>
        </div>
        <div>
            <!--
            <div>
                <p style="font-size: 17px;">Number of respondents </p>
                <input id="search" type="number" v-model="limit"/>
            </div>
            <br>
            -->
            <div>
                <!-- <p style="font-size: 17px;" >Filter  </p> -->
                <i class="material-icons">search</i>
                <input type="text" v-model="query"/>
            </div>
        </div>
        <div>
            <div @click="selectManager(mngr)" class="manager" v-for="mngr in filtered"> <!-- .slice(0, limit)"> -->
                <b v-html="renderField(fullName(mngr))"></b>
                <span style="padding-left: 10px;"></span>
                <small class="email-address" v-html="renderField(mngr.emailAddress)"></small><br>
                <small class="function" v-html="renderField(mngr.function)"></small><br>
                <small class="job-title" v-html="renderField(mngr.jobTitle)"></small>
            </div>
        </div>
    </div>
</template>
<script>
import { object } from '../utils';


export default {
    name: 'app-managers-search',
    props: ['managers'],
    data: function() {
        const self = this;
        return {
            query: '',
            filtered: [],
            limit: 10,

            selected: null,


            MANAGERS_FIELDS_TO_SEARCH: [
                { key: 'fullName', getter: self.FIBIMETER_CONSTANTS.getFullNameFromEmployee},
                'emailAddress',
                'function',
                'jobTitle',
            ],
        };
    },
    methods: {
        fullName(mngr) {
            if(mngr.fullName) {
                return mngr.fullName;
            }
            mngr.fullName = this.FIBIMETER_CONSTANTS.getFullNameFromEmployee(mngr);
            return mngr.fullName;
        },
        renderField(field) {
            if (!field) {
                return '';
            }

            if (typeof field === 'string') {
                return field;
            }

            const { val, hit } = field;
            const { start, end, len } = hit;
            return val.substring(0, start - 1)
                + '<em>'
                + val.substring(start, end)
                + '</em>'
                + val.substring(end, val.length);
        },
        selectManager(mngr) {
            this.$emit('onSelectManager', mngr);
        },
    },
    components: {

    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
        });
    },
    watch: {
        managers(val, oldVal) {
            this.filtered = search.call(this, this.query, this.managers);
        },
        query(val, oldVal) {
            if(val !== oldVal) {
                this.filtered = search.call(this, val, this.managers);
            }
        },
    }
}

function search(query, managers) {
    if(query === '') {
        return object.copy(managers);
    }

    const filtered = [];
    managers.forEach((mngr) => {
        let cmngr = null;
        let score = 0;
        this.MANAGERS_FIELDS_TO_SEARCH.forEach((field, i) => {
            let f = field;
            let getter = (mngr) => mngr[f];
            if(typeof field === 'object') {
                f = field.key;
                getter = field.getter;
            }

            if(!mngr[f]) {
                mngr[f] = getter(mngr);
            }

            let s = mngr[f];
            if(!s) {
                return;
            }
            if(typeof s === 'object' && typeof s.val !== 'undefined') {
                s = s.val;
            }
            const sl = s.toLowerCase();
            const q = query.toLowerCase();
            const index = sl.indexOf(q);
            if(index > -1) {
                const cp = object.copy(cmngr ? cmngr : mngr);
                const hit = {
                    field: f,
                    start: index,
                    end: index + query.length,
                    len: query.length,
                };
                const val = {
                    val: s,
                    hit,
                }
                cp[f] = val;
                cmngr = cp;
                score++;
                score += additionalScore(q, sl);
            }
        });
        if(cmngr) {
            cmngr.score = score;
            filtered.push(cmngr);
        }
    });
    return filtered.sort((mngr1, mngr2) => {
        return mngr2.score - mngr1.score;
    });
}

function additionalScore(q, s) {
    let add = 0;
    if(s.startsWith(q)) {
        add += 0.35;
    }
    if(s.endsWith(q)) {
        add += 0.23;
    }
    return add;
}

</script>
<style>
    .managers-search {

        input[type=text]:not(.browser-default) {
            width: 25%;
            text-align: center;
        }

        input[type=number]:not(.browser-default) {
            text-align: center;
            width: 15%;

        }

        input[type=number]:not(.browser-default):focus:not([readonly]) {
            border-bottom: 1px solid #e20074;
            -webkit-box-shadow: 0 1px 0 0 #e20074b3;
            box-shadow: 0 1px 0 0 #e20074b3;
        }

        input[type=text]:not(.browser-default):focus:not([readonly]) {
            border-bottom: 1px solid #e20074;
            -webkit-box-shadow: 0 1px 0 0 #e20074b3;
            box-shadow: 0 1px 0 0 #e20074b3;
        }

        .manager {
            color: #5e0063;
            padding: 5px;
            border-bottom: 1px solid #e2b0e1;
            cursor: pointer;

        em {
            background-color: #ffbdee;
            font-style: normal;
            border-radius: 3px;
        }
    }
}
</style>
