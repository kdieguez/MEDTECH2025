import { writable } from 'svelte/store';

export const accessToken = writable(null);
export const userRol = writable(null);
export const userCorreo = writable(null);
export const userEstado = writable(false);
export const permisoEditarPaginas = writable(false);
