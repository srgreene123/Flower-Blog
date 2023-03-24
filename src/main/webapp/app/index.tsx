import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { bindActionCreators } from 'redux';

import getStore from 'app/config/store';
import { registerLocale } from 'app/config/translation';
import setupAxiosInterceptors from 'app/config/axios-interceptor';
import { clearAuthentication } from 'app/shared/reducers/authentication';
import ErrorBoundary from 'app/shared/error/error-boundary';
import AppComponent from 'app/app';
import { loadIcons } from 'app/config/icon-loader';
import { ButtonGroup, Text, ChakraProvider, Container, IconButton, Stack } from '@chakra-ui/react';

const store = getStore();
registerLocale(store);

const actions = bindActionCreators({ clearAuthentication }, store.dispatch);
setupAxiosInterceptors(() => actions.clearAuthentication('login.error.unauthorized'));

loadIcons();

const rootEl = document.getElementById('root');
const root = createRoot(rootEl);

const render = Component =>
  root.render(
    <ChakraProvider>
      <ErrorBoundary>
        <Provider store={store}>
          <div>
            <Component />
          </div>
          <Container as="footer" role="contentinfo" py={{ base: '12', md: '16' }}>
            <Stack spacing={{ base: '4', md: '5' }}>
              <Stack justify="space-between" direction="row" align="center">
                {/* <Logo /> */}
                <ButtonGroup variant="ghost">
                  {/* <IconButton as="a" href="#" aria-label="LinkedIn" icon={<FaLinkedin fontSize="1.25rem" />} /> */}
                  {/* <IconButton as="a" href="#" aria-label="GitHub" icon={<FaGithub fontSize="1.25rem" />} /> */}
                  {/* <IconButton as="a" href="#" aria-label="Twitter" icon={<FaTwitter fontSize="1.25rem" />} /> */}
                </ButtonGroup>
              </Stack>
              <Text fontSize="sm" color="subtle">
                {/* Blooming */}
                {/* &copy; {new Date().getFullYear()} Chakra UI Pro, Inc. All rights reserved. */}
              </Text>
            </Stack>
          </Container>
        </Provider>
      </ErrorBoundary>
    </ChakraProvider>
  );

render(AppComponent);
