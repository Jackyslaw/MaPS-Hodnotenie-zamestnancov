if(typeof window.atob === 'undefined') {
    
    window.atob = function() {
        const b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
        const b64re = /^(?:[A-Za-z\d+\/]{4})*?(?:[A-Za-z\d+\/]{2}(?:==)?|[A-Za-z\d+\/]{3}=?)?$/;

        string = String(string).replace(/[\t\n\f\r ]+/g, "");
        if (!b64re.test(string))
            throw new TypeError("Failed to execute 'atob' on 'Window': The string to be decoded is not correctly encoded.");

        // Adding the padding if missing, for semplicity
        string += "==".slice(2 - (string.length & 3));
        var bitmap, result = "", r1, r2, i = 0;
        for (; i < string.length;) {
            bitmap = b64.indexOf(string.charAt(i++)) << 18 | b64.indexOf(string.charAt(i++)) << 12
                    | (r1 = b64.indexOf(string.charAt(i++))) << 6 | (r2 = b64.indexOf(string.charAt(i++)));

            result += r1 === 64 ? String.fromCharCode(bitmap >> 16 & 255)
                    : r2 === 64 ? String.fromCharCode(bitmap >> 16 & 255, bitmap >> 8 & 255)
                    : String.fromCharCode(bitmap >> 16 & 255, bitmap >> 8 & 255, bitmap & 255);
        }
        return result;
    }
}



export const object = {
    flatten: array => {
        const finalObj = {};
        array.map(obj => Object.keys(obj).map(
            key => finalObj[key] = typeof obj[key] === 'array'
                                    ? object.flatten(obj[key])
                                    : obj[key])
        );
        return finalObj;
    },
    copy: obj => JSON.parse(JSON.stringify(obj)),
}

export const jwt = {
    decode: t => ({
        raw: t,
        header: JSON.parse(window.atob(t.split('.')[0])),
        payload: JSON.parse(window.atob(t.split('.')[1])),
    }),
}

export const stringUtil = {
    capitalizeFirstLetter: str => str.charAt(0).toUpperCase() + str.slice(1),

    generateID: () => (Date.now().toString(36) + Math.random().toString(36).substr(2, 5)).toUpperCase(),
}

export const datetime = {
    miliseconds: (hrs,min,sec) => (( (hrs || 0) * 60 * 60 + (min || 0) * 60 + (sec || 0))),
}