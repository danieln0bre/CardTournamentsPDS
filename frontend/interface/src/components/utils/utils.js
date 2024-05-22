// src/utils/utils.js
export const generateUrlFriendlyName = (name) => {
    return encodeURIComponent(name.trim().replace(/\s+/g, '-'));
};

