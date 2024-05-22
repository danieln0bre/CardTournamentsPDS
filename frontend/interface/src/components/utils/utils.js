export function generateUrlFriendlyName(name) {
    return name.toLowerCase().replace(/\s+/g, '-');
}
